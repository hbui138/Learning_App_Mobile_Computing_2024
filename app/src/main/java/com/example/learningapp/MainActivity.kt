package com.example.learningapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.learningapp.ui.theme.LearningAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LearningAppTheme {
                Surface(modifier = Modifier.systemBarsPadding()) {
                    MyApp()
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp() {
    val navController = rememberNavController()
    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("Learning App") },
                // Back button
                navigationIcon = {
                    IconButton(onClick = {
                        val currentRoute = navController.currentDestination?.route
                        when (currentRoute) {
                            Screen.Settings.route,
                            Screen.LearnMenu.route,
                            Screen.WriteMenu.route -> {
                                navController.popBackStack(
                                    Screen.Menu.route,
                                    inclusive = false
                                )
                            }
                            else -> {
                                navController.navigateUp()
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                // Two action buttons
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Menu.route) }) {
                        Icon(Icons.Filled.Home, "Menu")
                    }
                    IconButton(onClick = {
                        val currentRoute = navController.currentDestination?.route
                        if (currentRoute == Screen.Settings.route) {
                            navController.navigate(Screen.Menu.route) {
                                popUpTo(Screen.Menu.route) { inclusive = true }
                            }
                        } else {
                            navController.navigate(Screen.Settings.route)
                        }
                    }) {
                        Icon(Icons.Filled.Settings, "Settings")
                    }
                },
                // top app bar color
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column (
            modifier = Modifier.padding(innerPadding),
        ) {
            AppNavGraph(
                navController = navController
            )
        }
    }
}