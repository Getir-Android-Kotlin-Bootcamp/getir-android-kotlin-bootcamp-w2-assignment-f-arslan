package com.getir.patika.foodmap

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class MapViewModel : ViewModel() {
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val _uiState = MutableStateFlow(MapUiState())
    val uiState = _uiState.asStateFlow()

    fun onQueryTextChange(query: String?) = viewModelScope.launch {
        _uiState.update { it.copy(query = query ?: "") }
    }

    fun onPermissionStateChanged(permissionState: PermissionState) = viewModelScope.launch {
        _uiState.update { it.copy(permissionState = permissionState) }
    }

    fun getCurrentLocation(context: Context) = viewModelScope.launch {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            _uiState.update { it.copy(permissionState = PermissionState.NEED_REQUEST) }
            return@launch
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
}
