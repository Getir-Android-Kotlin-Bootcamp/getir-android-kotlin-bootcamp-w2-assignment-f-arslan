//[app](../../../index.md)/[com.getir.patika.foodmap.data](../index.md)/[LocationRepository](index.md)

# LocationRepository

interface [LocationRepository](index.md)

Repository interface for handling location-related operations.

#### Inheritors

| |
|---|
| [LocationDataSource](../../com.getir.patika.foodmap.data.impl/-location-data-source/index.md) |

## Functions

| Name | Summary |
|---|---|
| [getCurrentLocation](get-current-location.md) | [androidJvm]<br>abstract suspend fun [getCurrentLocation](get-current-location.md)(): [LocationResult](../../com.getir.patika.foodmap.ui/-location-result/index.md)<br>Retrieves the current location of the device. |
| [getPlaceDetails](get-place-details.md) | [androidJvm]<br>abstract suspend fun [getPlaceDetails](get-place-details.md)(placeId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [LocationResult](../../com.getir.patika.foodmap.ui/-location-result/index.md)<br>Fetches the details of a place given its unique identifier. |
| [searchPlaces](search-places.md) | [androidJvm]<br>abstract suspend fun [searchPlaces](search-places.md)(query: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[AutoCompleteResult](../../com.getir.patika.foodmap.ui/-auto-complete-result/index.md)&gt;<br>Searches for places matching the given query string. |
