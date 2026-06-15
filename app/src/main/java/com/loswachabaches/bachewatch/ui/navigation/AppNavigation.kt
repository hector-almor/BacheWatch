package com.loswachabaches.bachewatch.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.loswachabaches.bachewatch.ui.screens.login.LoginScreen
import com.loswachabaches.bachewatch.ui.screens.login.RegisterScreen
import com.loswachabaches.bachewatch.ui.screens.mainscreen.MainScreen
import com.loswachabaches.bachewatch.ui.screens.registrarbache.RegistrarBacheScreen
import com.loswachabaches.bachewatch.ui.viewmodels.AuthUiState
import com.loswachabaches.bachewatch.ui.viewmodels.AuthViewModel
import com.loswachabaches.bachewatch.ui.viewmodels.ReporteUiState
import com.loswachabaches.bachewatch.ui.viewmodels.ReporteViewModel

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val MAIN = "main"
    const val REGISTRAR_BACHE = "registrar_bache"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    // Que se comparta con otras pantallas
    val authViewModel: AuthViewModel = viewModel()
    val reporteViewModel: ReporteViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(route = Routes.LOGIN) {
            val uiState by authViewModel.uiState.collectAsState()
            LoginScreen(
                uiState = uiState,
                onLoginClick = { correo, password ->
                    authViewModel.login(correo, password)
                },
                onRegisterClick = {
                    navController.navigate(Routes.REGISTER) {
                        launchSingleTop = true
                    }
                }
            )
            LaunchedEffect(uiState) {
                if (uiState is AuthUiState.Exito) {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
        }


        composable(route = Routes.REGISTER) {
            val uiState by authViewModel.uiState.collectAsState()

            RegisterScreen(
                uiState = uiState,
                onRegisterClick = { userData ->
                    authViewModel.registrar(userData.nombre, userData.correo, userData.password)
                },
                onLoginClick = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
            LaunchedEffect(uiState) {
                if (uiState is AuthUiState.Exito) {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
        }


        composable(route = Routes.MAIN) {
            MainScreen(
                authViewModel = authViewModel,
                reporteViewModel = reporteViewModel,
                onAddClick = {
                    navController.navigate(Routes.REGISTRAR_BACHE)
                },
                onLogoutClick = {
                    authViewModel.logout()
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
            val uiState by reporteViewModel.uiState.collectAsState()

            RegistrarBacheScreen(
                uiState = uiState,
                onCancelClick = {
                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                },
                onSaveClick = { photoUri, location, descripcion ->
                    val uid = authViewModel.obtenerUsuarioActual()?.uid ?: return@RegistrarBacheScreen
                    if (photoUri != null && location != null) {
                        reporteViewModel.crearReporte(
                            usuarioId = uid,
                            descripcion = descripcion,
                            latitud = location.latitude,
                            longitud = location.longitude,
                            direccionAproximada = "",
                            fotoUri = photoUri
                        )
                    }
                }
            )
            LaunchedEffect(uiState) {
                if (uiState is ReporteUiState.Exito) {
                    reporteViewModel.resetUiState()
                    navController.popBackStack()
                }
            }
        }
    }
}