package com.getir.patika.foodmap.data.impl

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.util.Log
import com.getir.patika.foodmap.ui.AutoCompleteResult
import com.getir.patika.foodmap.ui.LocationResult
import com.getir.patika.foodmap.data.LocationRepository
import com.getir.patika.foodmap.data.ext.toLocationState
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * A data source for location-related operations, implementing the [LocationRepository] interface.
 * This class uses the Places API client to fetch location data.
 *
 * @property placesClient The Places API client used for location operations.
 * @property ioDispatcher The coroutine dispatcher for performing IO operations.
 * @property context The application context.
 */
class LocationDataSource @Inject constructor(
    private val placesClient: PlacesClient,
    private val ioDispatcher: CoroutineDispatcher,
    @ApplicationContext private val context: Context
) : LocationRepository {

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): LocationResult = withContext(ioDispatcher) {
        if (!isGpsEnabled()) {
            return@withContext LocationResult.Error("GPS is not enabled")
        }

        val completableDeferred = CompletableDeferred<LocationResult>()
        val placeFields = listOf(Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.NAME)
        val request = FindCurrentPlaceRequest.newInstance(placeFields)

        placesClient.findCurrentPlace(request)
            .addOnSuccessListener { response ->
                response.placeLikelihoods.firstOrNull()?.place?.run {
                    latLng?.run {
                        completableDeferred.complete(toLocationState())
                    } ?: completableDeferred.complete(LocationResult.Error("LatLng is null"))
                } ?: completableDeferred.complete(LocationResult.Error("Place not found"))

            }
            .addOnFailureListener {
                completableDeferred.complete(
                    LocationResult.Error("Failed to fetch current location: ${it.message}")
                )
                Log.e("LocationDataSource", "An error occurred", it)
            }

        completableDeferred.await()
    }

    override suspend fun getPlaceDetails(placeId: String): LocationResult =
        withContext(ioDispatcher) {
            if (!isGpsEnabled()) {
                return@withContext LocationResult.Error("GPS is not enabled")
            }

            val completableDeferred = CompletableDeferred<LocationResult>()

            val placeFields = listOf(Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.NAME)
            val request = FetchPlaceRequest.newInstance(placeId, placeFields)
            placesClient.fetchPlace(request)
                .addOnSuccessListener { response ->
                    if (response != null && response.place.latLng != null) {
                        completableDeferred.complete(response.place.toLocationState())
                    } else {
                        completableDeferred.complete(LocationResult.Error("Place not found"))
                    }
                }
                .addOnFailureListener {
                    completableDeferred.complete(
                        LocationResult.Error("Failed to fetch place details: ${it.message}")
                    )
                    Log.e("LocationDataSource", "An error occurred", it)
                }

            completableDeferred.await()
        }


    override suspend fun searchPlaces(query: String): List<AutoCompleteResult> =
        withContext(ioDispatcher) {
            if (!isGpsEnabled()) {
                return@withContext emptyList()
            }

            val completeDeferred = CompletableDeferred<List<AutoCompleteResult>>()

            val request = FindAutocompletePredictionsRequest
                .builder()
                .setQuery(query)
                .build()
            placesClient
                .findAutocompletePredictions(request)
                .addOnSuccessListener { response ->
                    response.autocompletePredictions.map {
                        AutoCompleteResult(it.getFullText(null).toString(), it.placeId)
                    }.take(5).let {
                        completeDeferred.complete(it)
                    }
                }.addOnFailureListener {
                    completeDeferred.complete(emptyList())
                    Log.e("LocationDataSource", "An error occurred", it)
                }

            completeDeferred.await()
        }

    private fun isGpsEnabled(): Boolean {
        val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
}
