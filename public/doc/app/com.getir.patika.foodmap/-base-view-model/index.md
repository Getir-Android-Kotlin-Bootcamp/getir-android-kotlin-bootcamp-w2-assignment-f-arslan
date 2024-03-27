//[app](../../../index.md)/[com.getir.patika.foodmap](../index.md)/[BaseViewModel](index.md)

# BaseViewModel

open class [BaseViewModel](index.md) : [ViewModel](https://developer.android.com/reference/kotlin/androidx/lifecycle/ViewModel.html)

Base class for a ViewModel that provides a common coroutine launch mechanism with exception handling.

This class extends [ViewModel](https://developer.android.com/reference/kotlin/androidx/lifecycle/ViewModel.html) and should be used as a base class for other ViewModels to inherit common behavior.

#### Inheritors

| |
|---|
| [MapViewModel](../../com.getir.patika.foodmap.ui/-map-view-model/index.md) |

## Constructors

| | |
|---|---|
| [BaseViewModel](-base-view-model.md) | [androidJvm]<br>constructor() |

## Functions

| Name | Summary |
|---|---|
| [addCloseable](../../com.getir.patika.foodmap.ui/-map-view-model/index.md#264516373%2FFunctions%2F-912451524) | [androidJvm]<br>open fun [addCloseable](../../com.getir.patika.foodmap.ui/-map-view-model/index.md#264516373%2FFunctions%2F-912451524)(@[NonNull](https://developer.android.com/reference/kotlin/androidx/annotation/NonNull.html)p0: [Closeable](https://developer.android.com/reference/kotlin/java/io/Closeable.html)) |
| [launchCatching](launch-catching.md) | [androidJvm]<br>fun [launchCatching](launch-catching.md)(block: suspend CoroutineScope.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): Job<br>Launches a new coroutine in the [viewModelScope](https://developer.android.com/reference/kotlin/androidx/lifecycle/package-summary.html) and catches any exceptions that are thrown in the coroutine block. |
