package com.mairwunnx.myapplication

import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Adb
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.Navigator
import com.arkivanov.decompose.Router
import com.arkivanov.decompose.extensions.compose.jetpack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.animation.child.crossfade
import com.arkivanov.decompose.extensions.compose.jetpack.asState
import com.mairwunnx.myapplication.ui.theme.MyApplicationTheme
import kotlinx.parcelize.Parcelize

fun build(str: String) = buildString { repeat(200) { append("$str\n") } }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val router =
                rememberRouter<Screen>(
                    initialConfiguration = { Screen.First }
                )

            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Scaffold(
                        bottomBar = { Navigation(router) }
                    ) {
                        Children(
                            routerState = router.state,
                            animation = crossfade()
                        ) { child ->
                            when (val screen = child.configuration) {
                                is Screen.First ->
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(16.dp)
                                            .verticalScroll(rememberScrollState())
                                    ) {
                                        Text(text = build("bug"))
                                    }

                                is Screen.Second ->
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(16.dp)
                                            .verticalScroll(rememberScrollState())
                                    ) {
                                        Text(text = build("Yoloooooooo"))
                                    }

                                is Screen.Third ->
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(16.dp)
                                            .verticalScroll(rememberScrollState())
                                    ) {
                                        Text(text = build("Third screen"))
                                    }
                            }
                        }.let {}
                    }
                }
            }
        }
    }
}

private sealed class Screen : Parcelable {
    @Parcelize
    object First : Screen()

    @Parcelize
    object Second : Screen()

    @Parcelize
    object Third : Screen()
}

inline fun <C : Parcelable, reified Screen : C> Navigator<C>.pushOrActivate(
    crossinline configuration: () -> Screen
) {
    navigate { stack ->
        val existingConfiguration = stack.findLast { it is Screen }
        if (existingConfiguration != null) {
            stack.filterNot { it === existingConfiguration } + existingConfiguration
        } else {
            stack + configuration()
        }
    }
}

@Composable
private fun Navigation(router: Router<Screen, *>) =
    BottomNavigation {
        val routerState by router.state.asState()
        val activeScreen = routerState.activeChild.configuration

        BottomNavigationItem(
            icon = { Icon(Icons.Default.Favorite, "First screen") },
            label = { Text("First") },
            selected = activeScreen is Screen.First,
            onClick = {
                router.pushOrActivate { Screen.First }
            }
        )

        BottomNavigationItem(
            icon = { Icon(Icons.Default.Settings, "Second screen") },
            label = { Text("Second") },
            selected = activeScreen is Screen.Second,
            onClick = {
                router.pushOrActivate { Screen.Second }
            }
        )

        BottomNavigationItem(
            icon = { Icon(Icons.Default.Adb, "Third screen") },
            label = { Text("Third") },
            selected = activeScreen is Screen.Third,
            onClick = {
                router.pushOrActivate { Screen.Third }
            }
        )
    }
