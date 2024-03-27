//[app](../../../index.md)/[com.getir.patika.foodmap.data.impl](../index.md)/[LocationDataSource](index.md)

# LocationDataSource

[androidJvm]\
class [LocationDataSource](index.md)@Injectconstructor(placesClient: PlacesClient, ioDispatcher: CoroutineDispatcher, context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html)) : [LocationRepository](../../com.getir.patika.foodmap.data/-location-repository/index.md)

A data source for location-related operations, implementing the [LocationRepository](../../com.getir.patika.foodmap.data/-location-repository/index.md) interface. This class uses the Places API client to fetch location data.

## Constructors

| | |
|---|---|
| [LocationDataSource](-location-data-source.md) | [androidJvm]<br>@Inject<br>constructor(placesClient: PlacesClient, ioDispatcher: CoroutineDispatcher, context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html)) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [androidJvm]<br>object [Companion](-companion/index.md) |

## Functions

| Name | Summary |
|---|---|
| [getCurrentLocation](get-current-location.md) | [androidJvm]<br>open suspend override fun [getCurrentLocation](get-current-location.md)(): [LocationResult](../../com.getir.patika.foodmap.ui/-location-result/index.md)<br>Retrieves the current location of the device. |
| [getPlaceDetails](get-place-details.md) | [androidJvm]<br>open suspend override fun [getPlaceDetails](get-place-details.md)(placeId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [LocationResult](../../com.getir.patika.foodmap.ui/-location-result/index.md)<br>Fetches the details of a place given its unique identifier. |
| [searchPlaces](search-places.md) | [androidJvm]<br>open suspend override fun [searchPlaces](search-places.md)(query: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[AutoCompleteResult](../../com.getir.patika.foodmap.ui/-auto-complete-result/index.md)&gt;<br>Searches for places matching the given query string. |
