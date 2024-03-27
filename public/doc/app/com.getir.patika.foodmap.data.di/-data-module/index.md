//[app](../../../index.md)/[com.getir.patika.foodmap.data.di](../index.md)/[DataModule](index.md)

# DataModule

[androidJvm]\
@Module

object [DataModule](index.md)

A Hilt module that provides data-related dependencies for the application. This includes the Places API client, Preferences DataStore, and a coroutine dispatcher.

## Functions

| Name | Summary |
|---|---|
| [provideIODispatcher](provide-i-o-dispatcher.md) | [androidJvm]<br>@Singleton<br>@Provides<br>fun [provideIODispatcher](provide-i-o-dispatcher.md)(): CoroutineDispatcher |
| [providePlacesClient](provide-places-client.md) | [androidJvm]<br>@Singleton<br>@Provides<br>fun [providePlacesClient](provide-places-client.md)(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html)): PlacesClient |
| [providePreferencesDataStore](provide-preferences-data-store.md) | [androidJvm]<br>@Singleton<br>@Provides<br>fun [providePreferencesDataStore](provide-preferences-data-store.md)(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html)): [DataStore](https://developer.android.com/reference/kotlin/androidx/datastore/core/DataStore.html)&lt;[Preferences](https://developer.android.com/reference/kotlin/androidx/datastore/preferences/core/Preferences.html)&gt; |
