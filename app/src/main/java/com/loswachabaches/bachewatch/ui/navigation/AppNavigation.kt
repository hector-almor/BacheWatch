package com.loswachabaches.bachewatch.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.loswachabaches.bachewatch.ui.screens.login.LoginScreen
import com.loswachabaches.bachewatch.ui.screens.login.RegisterScreen
import com.loswachabaches.bachewatch.ui.screens.mainscreen.MainScreen
import com.loswachabaches.bachewatch.ui.screens.registrarbache.RegistrarBacheScreen

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
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
        composable(route = Routes.LOGIN) {
            LoginScreen(
                onLoginClick = {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.LOGIN) {
                            inclusive = true
                        }
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
                onRegisterClick = { userData ->
                    // Aquí después puedes guardar el usuario
                },
                onLoginClick = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.REGISTER) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }


        composable(route = Routes.MAIN) {
            MainScreen(
                onAddClick = {
                    navController.navigate(Routes.REGISTRAR_BACHE)
                },
                onLogoutClick = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.MAIN) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(route = Routes.REGISTRAR_BACHE) {
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