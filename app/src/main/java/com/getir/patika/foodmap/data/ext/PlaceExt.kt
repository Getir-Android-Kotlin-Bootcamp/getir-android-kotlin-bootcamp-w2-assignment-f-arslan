package com.getir.patika.foodmap.data.ext

import com.getir.patika.foodmap.Location
import com.getir.patika.foodmap.LocationResult
import com.google.android.libraries.places.api.model.Place

fun Place.toLocationState(): LocationResult {
    val latLng = requireNotNull(latLng) { "LatLng is null" }
    return LocationResult.Success(
        Location(
            latLng.latitude,
            latLng.longitude,
            address ?: "",
            name ?: ""
        )
    )
}
