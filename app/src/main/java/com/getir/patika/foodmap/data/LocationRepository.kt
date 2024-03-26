package com.getir.patika.foodmap.data

import com.getir.patika.foodmap.ui.AutoCompleteResult
import com.getir.patika.foodmap.ui.LocationResult

/**
 * Repository interface for handling location-related operations.
 */
interface LocationRepository {
    /**
     * Retrieves the current location of the device.
     *
     * @return [LocationResult] representing the current location or an error if the location
     *         cannot be determined.
     */
    suspend fun getCurrentLocation(): LocationResult

    /**
     * Fetches the details of a place given its unique identifier.
     *
     * @param placeId The unique identifier of the place to retrieve details for.
     * @return [LocationResult] containing the details of the place or an error if the details
     *         cannot be fetched.
     */
    suspend fun getPlaceDetails(placeId: String): LocationResult

    /**
     * Searches for places matching the given query string.
     *
     * @param query The search query string used to find places.
     * @return A list of [AutoCompleteResult] representing the search results.
     */
    suspend fun searchPlaces(query: String): List<AutoCompleteResult>
}
