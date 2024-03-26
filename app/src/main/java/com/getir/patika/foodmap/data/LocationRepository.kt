package com.getir.patika.foodmap.data

import com.getir.patika.foodmap.AutoCompleteResult
import com.getir.patika.foodmap.LocationResult

interface LocationRepository {
    suspend fun getCurrentLocation(): LocationResult
    suspend fun getPlaceDetails(placeId: String): LocationResult
    suspend fun searchPlaces(query: String): List<AutoCompleteResult>
}
