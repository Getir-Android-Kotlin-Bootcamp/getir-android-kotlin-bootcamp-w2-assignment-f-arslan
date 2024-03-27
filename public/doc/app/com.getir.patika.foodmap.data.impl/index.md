//[app](../../index.md)/[com.getir.patika.foodmap.data.impl](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [LocationDataSource](-location-data-source/index.md) | [androidJvm]<br>class [LocationDataSource](-location-data-source/index.md)@Injectconstructor(placesClient: PlacesClient, ioDispatcher: CoroutineDispatcher, context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html)) : [LocationRepository](../com.getir.patika.foodmap.data/-location-repository/index.md)<br>A data source for location-related operations, implementing the [LocationRepository](../com.getir.patika.foodmap.data/-location-repository/index.md) interface. This class uses the Places API client to fetch location data. |
| [PreferencesDataSource](-preferences-data-source/index.md) | [androidJvm]<br>class [PreferencesDataSource](-preferences-data-source/index.md)@Injectconstructor(userPreferences: [DataStore](https://developer.android.com/reference/kotlin/androidx/datastore/core/DataStore.html)&lt;[Preferences](https://developer.android.com/reference/kotlin/androidx/datastore/preferences/core/Preferences.html)&gt;) : [PreferencesRepository](../com.getir.patika.foodmap.data/-preferences-repository/index.md) |
