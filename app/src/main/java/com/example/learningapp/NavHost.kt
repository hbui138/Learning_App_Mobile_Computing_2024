package com.example.learningapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.languagelearningapp.ui.LearnScreen
import com.example.learningapp.ui.MenuScreen
import com.example.learningapp.ui.WriteScreen

// Define routes as a sealed class
sealed class Screen(val route: String) {
    object Menu : Screen("menu")
    object LearnMenu : Screen("learnMenu")
    object WriteMenu : Screen("writeMenu")
}

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier) {
    NavHost(navController = navController,
        modifier = modifier,
        startDestination = Screen.Menu.route
    ) {
        composable(Screen.Menu.route) {
            MenuScreen(navController)
        }
        composable(Screen.LearnMenu.route) {
            LearnScreen(navController)
        }
        composable(Screen.WriteMenu.route) {
            WriteScreen(navController)
        }
    }
}