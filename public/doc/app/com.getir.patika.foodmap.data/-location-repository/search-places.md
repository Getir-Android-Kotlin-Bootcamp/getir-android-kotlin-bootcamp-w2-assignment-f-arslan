//[app](../../../index.md)/[com.getir.patika.foodmap.data](../index.md)/[LocationRepository](index.md)/[searchPlaces](search-places.md)

# searchPlaces

[androidJvm]\
abstract suspend fun [searchPlaces](search-places.md)(query: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[AutoCompleteResult](../../com.getir.patika.foodmap.ui/-auto-complete-result/index.md)&gt;

Searches for places matching the given query string.

#### Return

A list of [AutoCompleteResult](../../com.getir.patika.foodmap.ui/-auto-complete-result/index.md) representing the search results.

#### Parameters

androidJvm

| | |
|---|---|
| query | The search query string used to find places. |
