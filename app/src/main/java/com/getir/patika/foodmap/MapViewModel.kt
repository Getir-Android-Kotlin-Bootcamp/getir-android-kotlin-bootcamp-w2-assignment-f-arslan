package com.getir.patika.foodmap

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
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

        fusedLocationProviderClient
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                location
                    ?.run {
                        _uiState.update {
                            it.copy(
                                locationState = LocationState.Success(Location(latitude, longitude))
                            )
                        }
                    }
                    ?: _uiState.update { it.copy(locationState = LocationState.Error("Location not found")) }
            }.addOnFailureListener { exception ->
                _uiState.update {
                    it.copy(
                        locationState = LocationState.Error(exception.message ?: "Unknown error")
                    )
                }
            }
    }

    private var searchJob: Job? = null
    private fun searchPlaces(query: String) = launchCatching {
        searchJob?.cancel()
        _autoCompleteResults.update { emptyList() }

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

    private fun getCoordinates(autoCompleteResult: AutoCompleteResult) =
        launchCatching {
            val placeFields = listOf(Place.Field.LAT_LNG)
            val request = FetchPlaceRequest.newInstance(autoCompleteResult.placeId, placeFields)
            placesClient.fetchPlace(request)
                .addOnSuccessListener { response ->
                    response?.let { fetchPlaceResponse ->
                        fetchPlaceResponse.place.also(::println)
                    }
                }
        }
}
