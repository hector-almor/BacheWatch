package com.loswachabaches.bachewatch.ui.navigation

import android.location.Geocoder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.loswachabaches.bachewatch.ui.screens.DetalleReporteScreen
import com.loswachabaches.bachewatch.ui.screens.EditarReporteScreen
import com.loswachabaches.bachewatch.ui.screens.login.LoginScreen
import com.loswachabaches.bachewatch.ui.screens.login.RegisterScreen
import com.loswachabaches.bachewatch.ui.screens.mainscreen.MainScreen
import com.loswachabaches.bachewatch.ui.screens.registrarbache.RegistrarBacheScreen
import com.loswachabaches.bachewatch.ui.viewmodels.AuthUiState
import com.loswachabaches.bachewatch.ui.viewmodels.AuthViewModel
import com.loswachabaches.bachewatch.ui.viewmodels.ReporteUiState
import com.loswachabaches.bachewatch.ui.viewmodels.ReporteViewModel
import java.util.Locale

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val MAIN = "main"
    const val REGISTRAR_BACHE = "registrar_bache"
    const val DETALLE_REPORTE = "detalle_reporte/{reporteId}"
    const val EDITAR_REPORTE = "editar_reporte/{reporteId}"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    // Que se comparta con otras pantallas
    val authViewModel: AuthViewModel = viewModel()
    val reporteViewModel: ReporteViewModel = viewModel()


    val context = LocalContext.current
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
                    authViewModel.obtenerUsuarioActual()?.uid?.let {
                        authViewModel.obtenerDatosUsuario(it)
                    }
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
                    authViewModel.obtenerUsuarioActual()?.uid?.let {
                        authViewModel.obtenerDatosUsuario(it) // <- agregar
                    }
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
        }


        composable(route = Routes.MAIN) {
            MainScreen(
                authViewModel    = authViewModel,
                reporteViewModel = reporteViewModel,
                onAddClick       = { navController.navigate(Routes.REGISTRAR_BACHE) },
                onLogoutClick    = {
                    authViewModel.logout()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.MAIN) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onReporteClick = { reporteId ->
                    navController.navigate("detalle_reporte/$reporteId")
                },
                onEditarClick = { reporteId ->
                    navController.navigate("editar_reporte/$reporteId")
                }
            )
        }

        composable(route = Routes.REGISTRAR_BACHE) {
            val uiState by reporteViewModel.uiState.collectAsState()
            val usuario by authViewModel.usuario.collectAsState()

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
                    val nombre = usuario?.nombre ?: "Usuario"
                    if (photoUri != null && location != null) {
                        val geocoder = Geocoder(context, Locale.getDefault())
                        val direccion = try {
                            val resultado = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                            resultado?.firstOrNull()?.getAddressLine(0) ?: ""
                        } catch (e: Exception) {
                            ""
                        }

                        reporteViewModel.crearReporte(
                            usuarioId = uid,
                            usuarioNombre = nombre,
                            descripcion = descripcion,
                            latitud = location.latitude,
                            longitud = location.longitude,
                            direccionAproximada = direccion,
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

        composable(route = Routes.DETALLE_REPORTE) { backStackEntry ->
            val reporteId = backStackEntry.arguments?.getString("reporteId") ?: return@composable
            val reportes by reporteViewModel.reportes.collectAsState()
            val reporte = reportes.find { it.id == reporteId } ?: return@composable

            DetalleReporteScreen(
                reporte = reporte,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(route = Routes.EDITAR_REPORTE) { backStackEntry ->
            val reporteId = backStackEntry.arguments?.getString("reporteId") ?: return@composable
            val reportes  by reporteViewModel.misReportes.collectAsState()
            val reporte   = reportes.find { it.id == reporteId } ?: return@composable
            val uiState   by reporteViewModel.uiState.collectAsState()

            EditarReporteScreen(
                reporte         = reporte,
                onBackClick     = { navController.popBackStack() },
                onGuardarClick  = { nuevaDescripcion ->
                    reporteViewModel.editarReporte(reporteId, nuevaDescripcion)
                },
                onEliminarClick = {
                    reporteViewModel.eliminarReporte(reporteId)
                }
            )

            LaunchedEffect(uiState) {
                if (uiState is ReporteUiState.Exito) {
                    reporteViewModel.resetUiState()
                    authViewModel.obtenerUsuarioActual()?.uid?.let {
                        reporteViewModel.obtenerMisReportes(it)
                    }
                    navController.popBackStack()
                }
            }
        }
    }
}