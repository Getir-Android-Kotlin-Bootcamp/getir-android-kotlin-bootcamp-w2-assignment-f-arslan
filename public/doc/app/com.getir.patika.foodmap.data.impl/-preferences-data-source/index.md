//[app](../../../index.md)/[com.getir.patika.foodmap.data.impl](../index.md)/[PreferencesDataSource](index.md)

# PreferencesDataSource

[androidJvm]\
class [PreferencesDataSource](index.md)@Injectconstructor(userPreferences: [DataStore](https://developer.android.com/reference/kotlin/androidx/datastore/core/DataStore.html)&lt;[Preferences](https://developer.android.com/reference/kotlin/androidx/datastore/preferences/core/Preferences.html)&gt;) : [PreferencesRepository](../../com.getir.patika.foodmap.data/-preferences-repository/index.md)

## Constructors

| | |
|---|---|
| [PreferencesDataSource](-preferences-data-source.md) | [androidJvm]<br>@Inject<br>constructor(userPreferences: [DataStore](https://developer.android.com/reference/kotlin/androidx/datastore/core/DataStore.html)&lt;[Preferences](https://developer.android.com/reference/kotlin/androidx/datastore/preferences/core/Preferences.html)&gt;) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [androidJvm]<br>object [Companion](-companion/index.md) |

## Functions

| Name | Summary |
|---|---|
| [getGreetingDialogState](get-greeting-dialog-state.md) | [androidJvm]<br>open suspend override fun [getGreetingDialogState](get-greeting-dialog-state.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Retrieves the current state indicating whether the greeting dialog should be shown. |
| [setGreetingDialogState](set-greeting-dialog-state.md) | [androidJvm]<br>open suspend override fun [setGreetingDialogState](set-greeting-dialog-state.md)()<br>Persists the state indicating that the greeting dialog has been shown. |
