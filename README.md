# LazyColumnItemDiffAnimations

An experimental method for providing item diff animations in a LazyColumn.

````
inline fun <T> LazyListScope.animatedItemsIndexed(
    state: List<AnimatedItem<T>>,
    enterTransition: EnterTransition = expandVertically(),
    exitTransition: ExitTransition = shrinkVertically(),
    noinline key: ((item: T) -> Any)? = null,
    crossinline itemContent: @Composable LazyItemScope.(index: Int, item: T) -> Unit
)
````

The `state` parameter should be obtained through the use of the `updateAnimatedItemsState` method:

````
fun <T> updateAnimatedItemsState(
    newList: List<T>
): State<List<AnimatedItem<T>>> 
````

Implementation is found [here](app/src/main/java/io/livekit/itemdiffs/ui/AnimatedLazyColumn.kt).

Example usage:

````
val animationState by updateAnimatedItemsState(list)
LazyColumn(Modifier.fillMaxSize()) {
    animatedItemsIndexed(animationState, key = { it }) { _, item ->
        // item content
        // ...
    }
}
````

This repo also provides an [example activity](app/src/main/java/io/livekit/itemdiffs/MainActivity.kt).
