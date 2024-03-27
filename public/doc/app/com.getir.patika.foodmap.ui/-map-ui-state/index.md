//[app](../../../index.md)/[com.getir.patika.foodmap.ui](../index.md)/[MapUiState](index.md)

# MapUiState

[androidJvm]\
data class [MapUiState](index.md)(val isLoading: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, val errorMessage: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) = &quot;&quot;, val query: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) = &quot;&quot;, val locationResult: [LocationResult](../-location-result/index.md) = LocationResult.Idle, val currentLocationResult: [LocationResult](../-location-result/index.md) = LocationResult.Idle, val dialogState: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false)

## Constructors

| | |
|---|---|
| [MapUiState](-map-ui-state.md) | [androidJvm]<br>constructor(isLoading: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, errorMessage: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) = &quot;&quot;, query: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) = &quot;&quot;, locationResult: [LocationResult](../-location-result/index.md) = LocationResult.Idle, currentLocationResult: [LocationResult](../-location-result/index.md) = LocationResult.Idle, dialogState: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false) |

## Properties

| Name | Summary |
|---|---|
| [currentLocationResult](current-location-result.md) | [androidJvm]<br>val [currentLocationResult](current-location-result.md): [LocationResult](../-location-result/index.md) |
| [dialogState](dialog-state.md) | [androidJvm]<br>val [dialogState](dialog-state.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false |
| [errorMessage](error-message.md) | [androidJvm]<br>val [errorMessage](error-message.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [isLoading](is-loading.md) | [androidJvm]<br>val [isLoading](is-loading.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false |
| [locationResult](location-result.md) | [androidJvm]<br>val [locationResult](location-result.md): [LocationResult](../-location-result/index.md) |
| [query](query.md) | [androidJvm]<br>val [query](query.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
