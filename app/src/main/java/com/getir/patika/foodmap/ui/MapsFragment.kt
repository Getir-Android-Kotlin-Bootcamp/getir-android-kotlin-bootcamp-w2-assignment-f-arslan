package com.getir.patika.foodmap.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.getir.patika.foodmap.BaseFragment
import com.getir.patika.foodmap.R
import com.getir.patika.foodmap.databinding.FragmentMapsBinding
import com.getir.patika.foodmap.ext.greetingRationaleDialog
import com.getir.patika.foodmap.ext.makeSnackbar
import com.getir.patika.foodmap.ext.makeToast
import com.getir.patika.foodmap.ext.openSettingsRationaleDialog
import com.getir.patika.foodmap.ext.toBitmapDescriptor
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

@AndroidEntryPoint
class MapsFragment : BaseFragment<FragmentMapsBinding>() {

    val viewModel: MapViewModel by viewModels()

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentMapsBinding =
        FragmentMapsBinding.inflate(inflater, container, false)

    override fun initializeViews() {
        setupFragment()
    }

    private fun setupFragment() {
        if (hasLocationPermission()) {
            viewModel.getCurrentLocation()
        }

        observeAutoCompleteResults()

        binding.run {
            observeLocationChanges()

            searchView.observeSearchViewChanges()

            ibMyLocation.myLocationOperations()

            requireContext().run {
                btnRing.setOnClickListener {
                    makeToast(R.string.ring_button_click)
                }

                btnBack.setOnClickListener {
                    makeToast(R.string.back_button_click)
                }

                btnSetLocation.setOnClickListener {
                    makeToast(R.string.set_location_button_click)
                }
            }
        }
    }

    private fun ImageButton.myLocationOperations() {
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions.any { it.value }) {
                viewModel.getCurrentLocation()
            }
        }

        setOnClickListener {
            checkLocationPermission(
                action = {
                    viewModel.getCurrentLocation()
                },
                launchPermission = {
                    requestPermissionLauncher.launch(arrayOf(FINE_LOCATION, COARSE_LOCATION))
                }
            )
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

    private var mMap: GoogleMap? = null
    private var currentMarker: Marker? = null
    private fun FragmentMapsBinding.observeLocationChanges() = scopeWithLifecycle {
        val pinIcon: BitmapDescriptor = R.drawable.pin.toBitmapDescriptor(requireContext())
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
                            mMap?.addMarker(
                                MarkerOptions().position(latLng).title(location.name).icon(pinIcon)
                            )
                        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                    }

                    LocationResult.Idle -> Unit
                }
            }
    }

    private fun observeAutoCompleteResults() = scopeWithLifecycle {
        viewModel.autoCompleteResults.collectLatest { results ->
            binding.rvSearchResult.adapter = AddressAdapter(results) {
                val focus = binding.searchView.findFocus()
                focus?.let { view ->
                    val inputMethodManager =
                        requireContext().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as? InputMethodManager
                    inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
                    view.clearFocus()
                }
                viewModel.getPlaceDetails(it)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(mapCallback)
    }

    private var initialMarker: Marker? = null
    private val mapCallback = OnMapReadyCallback { googleMap ->
        val pinIcon: BitmapDescriptor = R.drawable.pin.toBitmapDescriptor(requireContext())
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

    private fun clearMarkers() {
        currentMarker?.remove()
        initialMarker?.remove()
    }

    /**
     * Checks if the location permission has been granted and executes the provided action if so.
     * Otherwise, it shows the appropriate rationale dialog to the user.
     *
     * @param action The action to execute if the location permission is granted.
     */
    private fun checkLocationPermission(action: () -> Unit, launchPermission: () -> Unit) {
        val dialogState = viewModel.dialogState
        when {
            hasLocationPermission() -> action()
            !dialogState -> requireContext().greetingRationaleDialog {
                viewModel.setDialogState()
                launchPermission()
            }

            else -> requireContext().openSettingsRationaleDialog()
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

    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    requireContext(),
                    COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
        private const val COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
    }
}
