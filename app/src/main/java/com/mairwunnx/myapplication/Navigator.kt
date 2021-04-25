package com.mairwunnx.myapplication

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.LocalSaveableStateRegistry
import androidx.compose.runtime.saveable.SaveableStateRegistry
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.Router
import com.arkivanov.decompose.backpressed.BackPressedDispatcher
import com.arkivanov.decompose.lifecycle.Lifecycle
import com.arkivanov.decompose.lifecycle.LifecycleRegistry
import com.arkivanov.decompose.lifecycle.destroy
import com.arkivanov.decompose.lifecycle.resume
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.ParcelableContainer
import com.arkivanov.decompose.statekeeper.StateKeeper
import com.arkivanov.decompose.statekeeper.StateKeeperDispatcher
import kotlin.reflect.KClass

@Composable
inline fun <reified C : Parcelable> rememberRouter(
    noinline initialConfiguration: () -> C,
    noinline initialBackStack: () -> List<C> = ::emptyList,
    handleBackButton: Boolean = false
): Router<C, *> =
    rememberRouter(
        initialConfiguration = initialConfiguration,
        initialBackStack = initialBackStack,
        configurationClass = C::class,
        handleBackButton = handleBackButton
    )

@Composable
fun <C : Parcelable> rememberRouter(
    initialConfiguration: () -> C,
    initialBackStack: () -> List<C> = ::emptyList,
    configurationClass: KClass<out C>,
    handleBackButton: Boolean = false
): Router<C, *> {
    val context = componentContext()

    return remember {
        context.router(
            initialConfiguration = initialConfiguration,
            initialBackStack = initialBackStack,
            configurationClass = configurationClass,
            handleBackButton = handleBackButton
        ) { configuration, _ -> configuration }
    }
}

@Composable
fun componentContext(): ComponentContext {
    val lifecycle = lifecycle()
    val stateKeeper = stateKeeper()
    val backPressedDispatcher = LocalBackPressedDispatcher.current ?: BackPressedDispatcher()

    return remember {
        DefaultComponentContext(
            lifecycle = lifecycle,
            stateKeeper = stateKeeper,
            backPressedDispatcher = backPressedDispatcher
        )
    }
}

@Composable
private fun lifecycle(): Lifecycle {
    val lifecycle = remember { LifecycleRegistry() }

    DisposableEffect(Unit) {
        lifecycle.resume()
        onDispose { lifecycle.destroy() }
    }

    return lifecycle
}

@Composable
private fun stateKeeper(): StateKeeper {
    val saveableStateRegistry: SaveableStateRegistry? = LocalSaveableStateRegistry.current

    val dispatcher =
        remember {
            StateKeeperDispatcher(saveableStateRegistry?.consumeRestored(KEY_STATE) as ParcelableContainer?)
        }

    if (saveableStateRegistry != null) {
        DisposableEffect(Unit) {
            val entry = saveableStateRegistry.registerProvider(KEY_STATE, dispatcher::save)
            onDispose { entry.unregister() }
        }
    }

    return dispatcher
}

val LocalBackPressedDispatcher: ProvidableCompositionLocal<BackPressedDispatcher?> =
    staticCompositionLocalOf { null }

private const val KEY_STATE = "STATE"
