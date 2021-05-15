package io.livekit.itemdiffs.ui

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.*
import androidx.recyclerview.widget.DiffUtil

@ExperimentalAnimationApi
@Suppress("UpdateTransitionLabel", "TransitionPropertiesLabel", "unused")
@SuppressLint("ComposableNaming", "UnusedTransitionTargetStateParameter")
@Composable
inline fun <T> LazyListScope.animatedItems(
    animatedItemsState: AnimatedItemsState<List<T>>,
    enterTransition: EnterTransition = expandVertically(),
    exitTransition: ExitTransition = shrinkVertically(),
    noinline key: ((item: T) -> Any)? = null,
    crossinline itemContent: @Composable LazyItemScope.(item: T) -> Unit
) {
    animatedItemsIndexed(
        animatedItemsState = animatedItemsState,
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        key = key
    ) { _, item ->
        itemContent(item)
    }
}


@ExperimentalAnimationApi
@Suppress("UpdateTransitionLabel", "TransitionPropertiesLabel")
@SuppressLint("ComposableNaming", "UnusedTransitionTargetStateParameter")
/**
 * @param animatedItemsState Use [updateAnimatedItemsState].
 */
inline fun <T> LazyListScope.animatedItemsIndexed(
    animatedItemsState: AnimatedItemsState<List<T>>,
    enterTransition: EnterTransition = expandVertically(),
    exitTransition: ExitTransition = shrinkVertically(),
    noinline key: ((item: T) -> Any)? = null,
    crossinline itemContent: @Composable LazyItemScope.(index: Int, item: T) -> Unit
) {
    val (transitionState, transition, isAnimating) = animatedItemsState
    items(transitionState.currentState.size, if (key != null) { keyIndex: Int -> key(transitionState.currentState[keyIndex]) } else null) { index ->

        val item = transitionState.currentState[index]
        val isVisible = transitionState.targetState.contains(item)

        AnimatedVisibility(
            visible = isVisible,
            enter = enterTransition,
            exit = exitTransition,
            initiallyVisible = !isAnimating.value,
        ) {
            // These values aren't used, but rather to register the transition to save the old data
            // while the AnimatedVisibility is animating.
            transition.animateFloat {
                if (isVisible) 1f
                else 0f
            }
            itemContent(index, item)
        }
    }
}

@Composable
fun <T> updateAnimatedItemsState(
    targetState: List<T>,
    label: String? = null
): AnimatedItemsState<List<T>> {

    val transitionState = remember { MutableTransitionState(emptyList<T>()) }
    val transition = updateTransition(transitionState = transitionState, label = label)
    val isAnimating = remember(targetState) { mutableStateOf(true) }
    LaunchedEffect(targetState) {
        if (transitionState.targetState != targetState) {
            transitionState.targetState = targetState
        }
        val initialAnimation = Animatable(1.0f)
        try {
            initialAnimation.animateTo(0f)
        } finally {
            isAnimating.value = false
        }
    }

    return remember(transitionState, transition, isAnimating) { AnimatedItemsState(transitionState, transition, isAnimating) }
}

data class AnimatedItemsState<T>(
    val transitionState: MutableTransitionState<T>,
    val transition: Transition<T>,
    val isAnimating: State<Boolean>,
)