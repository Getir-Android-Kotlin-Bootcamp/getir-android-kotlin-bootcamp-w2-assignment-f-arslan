package com.getir.patika.foodmap

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.getir.patika.foodmap.databinding.ActivityMainBinding
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import com.getir.patika.foodmap.R.string as AppText

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
        Places.initialize(applicationContext, getString(AppText.api_key))

        viewModel.fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)
        viewModel.placesClient = Places.createClient(this)
        viewModel.getCurrentLocation(this)

        setupPermissions()
        setupMap()
        setupRecycleView()

        searchViewOperations()
        observeLocationChanges()
    }

    private fun setupRecycleView() = scopeWithLifecycle {
        binding.recycleViewSearchResult.layoutManager = LinearLayoutManager(this@MainActivity)
        viewModel.autoCompleteResults.collectLatest { results ->
            println(results)
            binding.recycleViewSearchResult.adapter = AddressAdapter(results) {
                viewModel.getCoordinates(it)
            }
        }
    }

    private fun observeLocationChanges() = scopeWithLifecycle {
        viewModel.uiState.map { it.locationState }.collectLatest {
            when (it) {
                is LocationState.Error -> {
                    binding.progressBar.visibility = View.GONE
                }

                LocationState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is LocationState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val location = it.location
                    val latLng = LatLng(location.latitude, location.longitude)
                    mMap.addMarker(MarkerOptions().position(latLng).title("Current Location"))
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                }
            }
        }
    }

    private fun setupPermissions() {
        scopeWithLifecycle {
            viewModel.uiState.map { it.permissionState }.collectLatest {
                if (!hasLocationPermission()) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            this@MainActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    ) {
                        showRationaleDialog()
                    }
                    requestLocationPermission()
                    viewModel.onPermissionStateChanged(PermissionState.IDLE)
                }
            }
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
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
            viewModel.uiState.map { it.query }.collectLatest { query ->
                binding.searchView.setQuery(query, false)
            }
        }
    }

    private fun scopeWithLifecycle(block: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED, block = block)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    private fun showRationaleDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(AppText.location_dialog_title))
            .setMessage(getString(AppText.location_dialog_message))
            .setPositiveButton(getString(AppText.ok)) { _, _ ->
                requestLocationPermission()
            }
            .setNegativeButton(getString(AppText.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}
