package com.loswachabaches.bachewatch.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.loswachabaches.bachewatch.ui.screens.login.LoginScreen
import com.loswachabaches.bachewatch.ui.screens.login.RegisterScreen
import com.loswachabaches.bachewatch.ui.screens.mainscreen.MainScreen

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val MAIN = "main"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(route = Routes.LOGIN) {
            LoginScreen(
                onLoginClick = {
                    navController.navigate(Routes.MAIN) {
                        launchSingleTop = true
                    }
                },
                onRegisterClick = {
                    navController.navigate(Routes.REGISTER) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(route = Routes.REGISTER) {
            RegisterScreen(
                onLoginClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = Routes.MAIN) {
            MainScreen()
        }
    }
}