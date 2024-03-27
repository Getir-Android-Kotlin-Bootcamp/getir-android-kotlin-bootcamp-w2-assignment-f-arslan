//[app](../../../index.md)/[com.getir.patika.foodmap.data.impl](../index.md)/[LocationDataSource](index.md)/[getPlaceDetails](get-place-details.md)

# getPlaceDetails

[androidJvm]\
open suspend override fun [getPlaceDetails](get-place-details.md)(placeId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [LocationResult](../../com.getir.patika.foodmap.ui/-location-result/index.md)

Fetches the details of a place given its unique identifier.

#### Return

[LocationResult](../../com.getir.patika.foodmap.ui/-location-result/index.md) containing the details of the place or an error if the details     cannot be fetched.

#### Parameters

androidJvm

| | |
|---|---|
| placeId | The unique identifier of the place to retrieve details for. |
