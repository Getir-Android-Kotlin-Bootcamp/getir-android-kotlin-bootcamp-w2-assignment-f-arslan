package com.getir.patika.foodmap.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.getir.patika.foodmap.R
import com.getir.patika.foodmap.ext.greetingRationaleDialog
import com.getir.patika.foodmap.ext.makeSnackbar
import com.getir.patika.foodmap.ext.makeToast
import com.getir.patika.foodmap.ext.openSettingsRationaleDialog
import com.getir.patika.foodmap.ext.setCustomFont
import com.getir.patika.foodmap.ext.toBitmapDescriptor
import com.getir.patika.foodmap.databinding.ActivityMainBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import com.getir.patika.foodmap.R.drawable as AppDrawable
import com.getir.patika.foodmap.R.string as AppText

/**
 * Main activity of the application, serving as the primary interface for user interactions
 * with the map and location features.
 *
 * This activity handles map initialization, location permissions, user input through a search view,
 * and displays location-related information and markers on the map.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mMap: GoogleMap

    val viewModel: MapViewModel by viewModels()

    /**
     * Initializes the activity, sets up the map, and configures UI interactions.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        handleInsets()
        setupMap()
        setupActivity()
    }

    private fun setupActivity() {
        if (hasLocationPermission()) {
            viewModel.getCurrentLocation()
        }

        observeAutoCompleteResults()

        binding.run {
            observeLocationChanges()

            searchView.observeSearchViewChanges()

            ibMyLocation.setOnClickListener {
                checkLocationPermission {
                    viewModel.getCurrentLocation()
                }
            }

            btnRing.setOnClickListener {
                makeToast(AppText.ring_button_click)
            }

            btnBack.setOnClickListener {
                makeToast(AppText.back_button_click)
            }

            btnSetLocation.setOnClickListener {
                makeToast(AppText.set_location_button_click)
            }

            searchView.setCustomFont()
        }
    }

    /**
     * Checks if the location permission has been granted and executes the provided action if so.
     * Otherwise, it shows the appropriate rationale dialog to the user.
     *
     * @param action The action to execute if the location permission is granted.
     */
    private fun checkLocationPermission(action: () -> Unit) {
        val dialogState = viewModel.dialogState
        when {
            hasLocationPermission() -> action()
            !dialogState -> greetingRationaleDialog {
                viewModel.setDialogState()
                requestMultiplePermissions()
            }

            else -> openSettingsRationaleDialog()
        }
    }

    private fun observeAutoCompleteResults() = scopeWithLifecycle {
        viewModel.autoCompleteResults.collectLatest { results ->
            binding.rvSearchResult.adapter = AddressAdapter(results) {
                val focus = binding.searchView.findFocus()
                focus?.let { view ->
                    val inputMethodManager =
                        getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
                    inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
                    view.clearFocus()
                }
                viewModel.getPlaceDetails(it)
            }
        }
    }

    private var currentMarker: Marker? = null
    private fun ActivityMainBinding.observeLocationChanges() = scopeWithLifecycle {
        val pinIcon: BitmapDescriptor = AppDrawable.pin.toBitmapDescriptor(this@MainActivity)
        viewModel.uiState.map { it.locationResult }.distinctUntilChanged()
            .collectLatest {
                when (it) {
                    is LocationResult.Error -> {
                        progressBar.visibility = View.GONE
                        tvYourLocationDetail.visibility = View.VISIBLE
                        makeSnackbar(it.errorMessage)
                    }

                    LocationResult.Loading -> {
                        tvYourLocationDetail.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE
                    }

                    is LocationResult.Success -> {
                        progressBar.visibility = View.GONE
                        tvYourLocationDetail.visibility = View.VISIBLE

                        val location = it.location
                        tvYourLocationDetail.text = location.address
                        val latLng = LatLng(location.latitude, location.longitude)
                        clearMarkers()

                        currentMarker =
                            mMap.addMarker(
                                MarkerOptions().position(latLng).title(location.name).icon(pinIcon)
                            )
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                    }

                    LocationResult.Idle -> Unit
                }
            }
    }

    private fun clearMarkers() {
        currentMarker?.remove()
        initialMarker?.remove()
    }

    private fun setupMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    /**
     * Handles window insets for the activity's view, adjusting padding to accommodate system bars.
     */
    private fun handleInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun SearchView.observeSearchViewChanges() {
        setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.onQueryTextChange(newText)
                return false
            }
        })

        scopeWithLifecycle {
            viewModel.uiState.map { it.query }.distinctUntilChanged().collectLatest { query ->
                setQuery(query, false)
            }
        }
    }

    /**
     * Creates a coroutine scope tied to the activity's lifecycle for executing suspending block of code.
     *
     * @param block The suspending block of code to execute within the lifecycle scope.
     */
    private fun scopeWithLifecycle(block: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED, block = block)
        }
    }

    private var initialMarker: Marker? = null

    /**
     * Callback method when the map is ready to be used. Sets up initial map configurations and markers.
     *
     * @param googleMap The GoogleMap instance ready for use.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        val pinIcon: BitmapDescriptor = AppDrawable.pin.toBitmapDescriptor(this@MainActivity)
        mMap = googleMap
        val latLng = LatLng(41.0082, 28.9784)
        googleMap.uiSettings.isCompassEnabled = false
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f), object :
            GoogleMap.CancelableCallback {
            override fun onFinish() {
                initialMarker = googleMap.addMarker(
                    MarkerOptions().position(latLng).title("Istanbul").icon(pinIcon)
                )
            }

            override fun onCancel() {}
        })
    }

    private fun requestMultiplePermissions() {
        requestPermissions(
            arrayOf(FINE_LOCATION, COARSE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    /**
     * Handles the result of the location permission request.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.any { it == PackageManager.PERMISSION_GRANTED }) {
            viewModel.getCurrentLocation()
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    this,
                    COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
        private const val COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}
