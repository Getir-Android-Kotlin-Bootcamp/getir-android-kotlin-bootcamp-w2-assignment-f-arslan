package com.getir.patika.foodmap

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.getir.patika.foodmap.databinding.ActivityMainBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import com.getir.patika.foodmap.R.string as AppText

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mMap: GoogleMap

    val viewModel: MapViewModel by viewModels()

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

        searchViewOperations()
        observeLocationChanges()

        binding.run {
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
        }
    }

    private fun makeToast(@StringRes text: Int) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun checkLocationPermission(action: () -> Unit) {
        val dialogState = viewModel.dialogState
        when {
            hasLocationPermission() -> action()
            !dialogState -> greetingRationaleDialog()
            else -> openSettingsRationaleDialog()
        }
    }

    private fun observeAutoCompleteResults() = scopeWithLifecycle {
        viewModel.autoCompleteResults.collectLatest { results ->
            binding.rvSearchResult.adapter = AddressAdapter(results) {
                val focus = binding.searchView.findFocus()
                focus?.let { view ->
                    val inputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
                    view.clearFocus()
                }
                viewModel.getPlaceDetails(it)
            }
        }
    }

    private var currentMarker: Marker? = null

    private fun observeLocationChanges() = scopeWithLifecycle {
        val icon: BitmapDescriptor = bitmapDescriptorFromVector(R.drawable.pin)

        viewModel.uiState.map { it.locationResult }.distinctUntilChanged()
            .collectLatest {
                when (it) {
                    is LocationResult.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.tvYourLocationDetail.visibility = View.VISIBLE
                    }

                    LocationResult.Loading -> {
                        binding.tvYourLocationDetail.visibility = View.GONE
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is LocationResult.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.tvYourLocationDetail.visibility = View.VISIBLE
                        val location = it.location
                        binding.tvYourLocationDetail.text = location.address
                        val latLng = LatLng(location.latitude, location.longitude)
                        clearMarkers()
                        currentMarker =
                            mMap.addMarker(
                                MarkerOptions().position(latLng).title(location.name).icon(icon)
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

    private fun bitmapDescriptorFromVector(vectorResId: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(this, vectorResId)
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun setupMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    private fun handleInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun searchViewOperations() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
                binding.searchView.setQuery(query, false)
            }
        }
    }

    private fun scopeWithLifecycle(block: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED, block = block)
        }
    }

    private var initialMarker: Marker? = null
    override fun onMapReady(googleMap: GoogleMap) {
        val icon: BitmapDescriptor = bitmapDescriptorFromVector(R.drawable.pin)
        mMap = googleMap
        val latLng = LatLng(41.0082, 28.9784)
        googleMap.uiSettings.isCompassEnabled = false
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f), object :
            GoogleMap.CancelableCallback {
            override fun onFinish() {
                initialMarker = googleMap.addMarker(
                    MarkerOptions().position(latLng).title("Istanbul").icon(icon)
                )
            }

            override fun onCancel() {}
        })
    }

    private fun openSettingsRationaleDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(AppText.location_dialog_title))
            .setMessage(getString(AppText.location_dialog_message))
            .setIcon(R.drawable.info)
            .setPositiveButton(getString(AppText.open_settings)) { dialog, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", packageName, null)
                }
                dialog.dismiss()
                startActivity(intent)
            }
            .setNegativeButton(getString(AppText.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun greetingRationaleDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(AppText.greeting_dialog_title))
            .setMessage(getString(AppText.greeting_dialog_message))
            .setIcon(R.drawable.waving_hand)
            .setPositiveButton(getString(AppText.ok)) { dialog, _ ->
                viewModel.setDialogState()
                requestMultiplePermissions()
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun requestMultiplePermissions() {
        requestPermissions(
            arrayOf(FINE_LOCATION, COARSE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

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
