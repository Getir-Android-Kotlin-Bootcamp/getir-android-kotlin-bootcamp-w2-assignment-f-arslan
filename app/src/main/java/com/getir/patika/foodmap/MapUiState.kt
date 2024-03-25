package com.getir.patika.foodmap

data class MapUiState(
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val query: String = "",
    val locationState: LocationState = LocationState.Success(
        Location(latitude = -34.0, longitude = 151.0)
    ),
    val permissionState: PermissionState = PermissionState.DENIED,
    val address: String = ""
)

enum class PermissionState {
    IDLE,
    DENIED,
    NEED_REQUEST
}

data class AutoCompleteResult(
    val fullText: String,
    val placeId: String
)

data class Location(
    val latitude: Double,
    val longitude: Double,
    val address: String = ""
)

sealed interface LocationState {
    data object Loading : LocationState
    data class Success(val location: Location) : LocationState
    data class Error(val errorMessage: String) : LocationState
}
