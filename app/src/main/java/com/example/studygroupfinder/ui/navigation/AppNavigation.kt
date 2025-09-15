package com.example.studygroupfinder.ui.navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.studygroupfinder.ui.AuthScreen
import com.example.studygroupfinder.ui.DashboardScreen
import com.example.studygroupfinder.ui.MapViewScreen
import com.example.studygroupfinder.ui.ProfileFormScreen
import com.example.studygroupfinder.ui.ProfileScreen
import com.example.studygroupfinder.ui.SettingsScreen
import com.example.studygroupfinder.ui.SplashScreen
import com.example.studygroupfinder.viewmodel.AuthViewModel

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Auth : Screen("auth")
    object ProfileForm : Screen("profile_form")
    object Dashboard : Screen("dashboard")
    object MapView : Screen("map_view")
    object Settings : Screen("settings")
    object Profile : Screen("profile")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }
        composable(Screen.Auth.route) {
            AuthScreen(navController = navController, authViewModel = authViewModel)
        }
        composable(Screen.ProfileForm.route) {
            ProfileFormScreen(navController = navController)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
        composable(Screen.Dashboard.route) {
            DashboardScreen(navController = navController, authViewModel = authViewModel)
        }
        composable(Screen.MapView.route) {
            MapViewScreen()
        }
        composable(Screen.Settings.route) {
            SettingsScreen()
        }
    }
}
