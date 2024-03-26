package com.getir.patika.foodmap

data class MapUiState(
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val query: String = "",
    val locationResult: LocationResult = LocationResult.Idle,
    val currentLocationResult: LocationResult = LocationResult.Idle,
    val dialogState: Boolean = false,
)

data class AutoCompleteResult(
    val fullText: String,
    val placeId: String
)

data class Location(
    val latitude: Double,
    val longitude: Double,
    val address: String = "",
    val name: String = ""
)

sealed class LocationResult {
    data object Idle: LocationResult()
    data object Loading : LocationResult()
    data class Success(val location: Location) : LocationResult()
    data class Error(val errorMessage: String) : LocationResult()
}
