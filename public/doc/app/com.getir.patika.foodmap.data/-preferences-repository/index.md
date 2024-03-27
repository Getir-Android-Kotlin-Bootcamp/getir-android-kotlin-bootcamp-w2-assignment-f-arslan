//[app](../../../index.md)/[com.getir.patika.foodmap.data](../index.md)/[PreferencesRepository](index.md)

# PreferencesRepository

interface [PreferencesRepository](index.md)

Interface for managing user preferences related to the greeting dialog state.

#### Inheritors

| |
|---|
| [PreferencesDataSource](../../com.getir.patika.foodmap.data.impl/-preferences-data-source/index.md) |

## Functions

| Name | Summary |
|---|---|
| [getGreetingDialogState](get-greeting-dialog-state.md) | [androidJvm]<br>abstract suspend fun [getGreetingDialogState](get-greeting-dialog-state.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Retrieves the current state indicating whether the greeting dialog should be shown. |
| [setGreetingDialogState](set-greeting-dialog-state.md) | [androidJvm]<br>abstract suspend fun [setGreetingDialogState](set-greeting-dialog-state.md)()<br>Persists the state indicating that the greeting dialog has been shown. |
