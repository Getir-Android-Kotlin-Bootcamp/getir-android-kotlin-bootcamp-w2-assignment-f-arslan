//[app](../../../index.md)/[com.getir.patika.foodmap.ui](../index.md)/[LocationResult](index.md)

# LocationResult

sealed class [LocationResult](index.md)

#### Inheritors

| |
|---|
| [Idle](-idle/index.md) |
| [Loading](-loading/index.md) |
| [Success](-success/index.md) |
| [Error](-error/index.md) |

## Types

| Name | Summary |
|---|---|
| [Error](-error/index.md) | [androidJvm]<br>data class [Error](-error/index.md)(val errorMessage: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) : [LocationResult](index.md) |
| [Idle](-idle/index.md) | [androidJvm]<br>data object [Idle](-idle/index.md) : [LocationResult](index.md) |
| [Loading](-loading/index.md) | [androidJvm]<br>data object [Loading](-loading/index.md) : [LocationResult](index.md) |
| [Success](-success/index.md) | [androidJvm]<br>data class [Success](-success/index.md)(val location: [Location](../-location/index.md)) : [LocationResult](index.md) |

## Functions

| Name | Summary |
|---|---|
| [toErrorState](to-error-state.md) | [androidJvm]<br>fun [toErrorState](to-error-state.md)(errorMessage: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [LocationResult](index.md) |
