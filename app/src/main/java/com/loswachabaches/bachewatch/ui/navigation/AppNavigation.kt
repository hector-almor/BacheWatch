package com.loswachabaches.bachewatch.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.loswachabaches.bachewatch.ui.screens.login.LoginScreen
import com.loswachabaches.bachewatch.ui.screens.mainscreen.MainScreen
import com.loswachabaches.bachewatch.ui.screens.registrarbache.RegistrarBacheScreen

object Routes {
    const val LOGIN = "login"
    const val MAIN = "main"
    const val REGISTRAR_BACHE = "registrar_bache"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginClick = {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.LOGIN) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Routes.MAIN) {
            MainScreen(
                onAddClick = {
                    navController.navigate(Routes.REGISTRAR_BACHE)
                }
            )
        }

        composable(Routes.REGISTRAR_BACHE) {
            RegistrarBacheScreen(
                onCancelClick = {
                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                },
                onSaveClick = { _, _ ->
                    navController.popBackStack()
                }
            )
        }
    }
}