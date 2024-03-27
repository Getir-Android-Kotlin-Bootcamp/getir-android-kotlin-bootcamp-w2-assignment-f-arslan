//[app](../../../index.md)/[com.getir.patika.foodmap.ui](../index.md)/[MapViewModel](index.md)

# MapViewModel

[androidJvm]\
class [MapViewModel](index.md)@Injectconstructor(locationRepository: [LocationRepository](../../com.getir.patika.foodmap.data/-location-repository/index.md), preferencesRepository: [PreferencesRepository](../../com.getir.patika.foodmap.data/-preferences-repository/index.md)) : [BaseViewModel](../../com.getir.patika.foodmap/-base-view-model/index.md)

ViewModel responsible for handling the map-related logic and user interactions. It communicates with [LocationRepository](../../com.getir.patika.foodmap.data/-location-repository/index.md) and [PreferencesRepository](../../com.getir.patika.foodmap.data/-preferences-repository/index.md) to fetch and store data.

## Constructors

| | |
|---|---|
| [MapViewModel](-map-view-model.md) | [androidJvm]<br>@Inject<br>constructor(locationRepository: [LocationRepository](../../com.getir.patika.foodmap.data/-location-repository/index.md), preferencesRepository: [PreferencesRepository](../../com.getir.patika.foodmap.data/-preferences-repository/index.md)) |

## Properties

| Name | Summary |
|---|---|
| [autoCompleteResults](auto-complete-results.md) | [androidJvm]<br>val [autoCompleteResults](auto-complete-results.md): StateFlow&lt;[List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[AutoCompleteResult](../-auto-complete-result/index.md)&gt;&gt; |
| [dialogState](dialog-state.md) | [androidJvm]<br>val [dialogState](dialog-state.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [uiState](ui-state.md) | [androidJvm]<br>val [uiState](ui-state.md): StateFlow&lt;[MapUiState](../-map-ui-state/index.md)&gt; |

## Functions

| Name | Summary |
|---|---|
| [addCloseable](index.md#264516373%2FFunctions%2F-912451524) | [androidJvm]<br>open fun [addCloseable](index.md#264516373%2FFunctions%2F-912451524)(@[NonNull](https://developer.android.com/reference/kotlin/androidx/annotation/NonNull.html)p0: [Closeable](https://developer.android.com/reference/kotlin/java/io/Closeable.html)) |
| [getCurrentLocation](get-current-location.md) | [androidJvm]<br>fun [getCurrentLocation](get-current-location.md)(): Job |
| [getPlaceDetails](get-place-details.md) | [androidJvm]<br>fun [getPlaceDetails](get-place-details.md)(autoCompleteResult: [AutoCompleteResult](../-auto-complete-result/index.md)): Job |
| [launchCatching](../../com.getir.patika.foodmap/-base-view-model/launch-catching.md) | [androidJvm]<br>fun [launchCatching](../../com.getir.patika.foodmap/-base-view-model/launch-catching.md)(block: suspend CoroutineScope.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): Job<br>Launches a new coroutine in the [viewModelScope](https://developer.android.com/reference/kotlin/androidx/lifecycle/package-summary.html) and catches any exceptions that are thrown in the coroutine block. |
| [onQueryTextChange](on-query-text-change.md) | [androidJvm]<br>fun [onQueryTextChange](on-query-text-change.md)(query: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?): Job<br>Handles the logic for updating the query text and triggering a place search. |
| [setDialogState](set-dialog-state.md) | [androidJvm]<br>fun [setDialogState](set-dialog-state.md)(): Job |
