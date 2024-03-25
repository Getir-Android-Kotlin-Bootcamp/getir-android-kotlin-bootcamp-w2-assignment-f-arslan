package com.getir.patika.foodmap

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MapViewModel : BaseViewModel() {
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var placesClient: PlacesClient

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState = _uiState.asStateFlow()

    private val _autoCompleteResults = MutableStateFlow(emptyList<AutoCompleteResult>())
    val autoCompleteResults = _autoCompleteResults.asStateFlow()

    fun onQueryTextChange(query: String?) = viewModelScope.launch {
        _uiState.update { it.copy(query = query ?: "") }
        query?.let { searchPlaces(it) }
    }

    fun onPermissionStateChanged(permissionState: PermissionState) = viewModelScope.launch {
        _uiState.update { it.copy(permissionState = permissionState) }
    }

    fun getCurrentLocation(context: Context) = launchCatching {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            _uiState.update { it.copy(permissionState = PermissionState.NEED_REQUEST) }
            return@launchCatching
        }
        _uiState.update { it.copy(locationState = LocationState.Loading) }

        val placeFields = listOf(Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.NAME)
        val request = FindCurrentPlaceRequest.newInstance(placeFields)
        placesClient.findCurrentPlace(request)
            .addOnSuccessListener { response ->
                response?.let { findCurrentPlaceResponse ->
                    findCurrentPlaceResponse.placeLikelihoods.firstOrNull()?.place?.run {
                        _uiState.update {
                            it.copy(locationState = toLocationState())
                        }
                    }
                } ?: Log.e("MapViewModel", "Place not found")
            }
            .addOnFailureListener {
                Log.e("MapViewModel", "An error occurred", it)
            }
    }

    private var searchJob: Job? = null
    private fun searchPlaces(query: String) {
        searchJob?.cancel()
        _autoCompleteResults.update { emptyList() }

        searchJob = launchCatching {
            val request = FindAutocompletePredictionsRequest
                .builder()
                .setQuery(query)
                .build()
            placesClient
                .findAutocompletePredictions(request)
                .addOnSuccessListener { response ->
                    _autoCompleteResults.update {
                        response.autocompletePredictions.map {
                            AutoCompleteResult(it.getFullText(null).toString(), it.placeId)
                        }.take(5)
                    }
                }.addOnFailureListener {
                    _autoCompleteResults.update { emptyList() }
                    Log.e("MapViewModel", "An error occurred", it)
                }
        }
    }

    fun getCoordinates(autoCompleteResult: AutoCompleteResult) = launchCatching {
        val placeFields = listOf(Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.NAME)
        val request = FetchPlaceRequest.newInstance(autoCompleteResult.placeId, placeFields)
        placesClient.fetchPlace(request)
            .addOnSuccessListener { response ->
                response?.let { fetchPlaceResponse ->
                    fetchPlaceResponse.place.run {
                        _uiState.update {
                            it.copy(locationState = toLocationState(), query = "")
                        }
                        _autoCompleteResults.update { emptyList() }
                    }
                } ?: Log.e("MapViewModel", "Place not found")
            }
            .addOnFailureListener {
                Log.e("MapViewModel", "An error occurred", it)
            }
    }

    private fun Place.toLocationState(): LocationState {
        val latLng = requireNotNull(latLng) { "Latitude and longitude must not be null" }
        return LocationState.Success(Location(latLng.latitude, latLng.longitude, address ?: "", name ?: ""))
    }
}
