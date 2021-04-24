package com.mairwunnx.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Adb
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.mairwunnx.myapplication.ui.theme.MyApplicationTheme

fun build(str: String) = buildString { repeat(200) { append("$str\n") } }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Scaffold(
                        bottomBar = { Navigation(navController) }
                    ) {
                        NavHost(navController, "first") {
                            composable("first") {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp)
                                        .verticalScroll(rememberScrollState())
                                ) {
                                    Text(text = build("bug") )
                                }
                            }
                            composable("second") {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp)
                                        .verticalScroll(rememberScrollState())
                                ) {
                                    Text(text = build("Yoloooooooo"))
                                }
                            }
                            composable("third") {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp)
                                        .verticalScroll(rememberScrollState())
                                ) {
                                    Text(text = build("Third screen"))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Navigation(navController: NavHostController) =
    BottomNavigation {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)

        BottomNavigationItem(
            icon = { Icon(Icons.Default.Favorite, "First screen") },
            label = { Text("First") },
            selected = currentRoute == "first",
            onClick = {
                navController.navigate("first") {
                    popUpTo = navController.graph.startDestination
                    launchSingleTop = true
                }
            }
        )

        BottomNavigationItem(
            icon = { Icon(Icons.Default.Settings, "Second screen") },
            label = { Text("Second") },
            selected = currentRoute == "second",
            onClick = {
                navController.navigate("second") {
                    popUpTo = navController.graph.startDestination
                    launchSingleTop = true
                }
            }
        )

        BottomNavigationItem(
            icon = { Icon(Icons.Default.Adb, "Third screen") },
            label = { Text("Third") },
            selected = currentRoute == "third",
            onClick = {
                navController.navigate("third") {
                    popUpTo = navController.graph.startDestination
                    launchSingleTop = true
                }
            }
        )
    }