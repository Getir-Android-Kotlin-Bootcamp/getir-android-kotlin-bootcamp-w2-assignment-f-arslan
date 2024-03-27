//[app](../../../index.md)/[com.getir.patika.foodmap](../index.md)/[BaseViewModel](index.md)/[launchCatching](launch-catching.md)

# launchCatching

[androidJvm]\
fun [launchCatching](launch-catching.md)(block: suspend CoroutineScope.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): Job

Launches a new coroutine in the [viewModelScope](https://developer.android.com/reference/kotlin/androidx/lifecycle/package-summary.html) and catches any exceptions that are thrown in the coroutine block.

#### Parameters

androidJvm

| | |
|---|---|
| block | The suspend lambda block to be executed within the coroutine.     It is a suspending function that will be scoped to CoroutineScope.     Any uncaught exceptions in this block will be handled by the     CoroutineExceptionHandler. |
