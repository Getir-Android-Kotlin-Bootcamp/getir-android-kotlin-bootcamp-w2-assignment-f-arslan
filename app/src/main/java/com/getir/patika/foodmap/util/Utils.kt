package com.getir.patika.foodmap.util

object Utils {
    const val GPS_NOT_ENABLED = "GPS is not enabled"
    const val COORDINATE_NOT_FOUND = "Coordinate not found"
    const val PLACE_NOT_FOUND = "Place not found"
    val failedToFetchCurrentLocation: (String?) -> String = {
        "Failed to fetch current location $it"
    }
    val failedToFetchPlaceDetails: (String?) -> String = {
        "Failed to fetch place details $it"
    }

    const val GENERIC_ERROR = "An error occurred"
}
