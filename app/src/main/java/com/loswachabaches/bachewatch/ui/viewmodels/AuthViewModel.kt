package com.loswachabaches.bachewatch.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loswachabaches.bachewatch.data.model.Usuario
import com.loswachabaches.bachewatch.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val authRepository = AuthRepository()

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun registrar(nombre: String, correo: String, contrasena: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Cargando
            val resultado = authRepository.registrar(nombre, correo, contrasena)
            _uiState.value = if (resultado.isSuccess) {
                AuthUiState.Exito
            } else {
                //AuthUiState.Error(resultado.exceptionOrNull()?.message ?: "Error desconocido")
                AuthUiState.Error(traducirError(resultado.exceptionOrNull()))
            }
        }
    }

    fun login(correo: String, contrasena: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Cargando
            val resultado = authRepository.login(correo, contrasena)
            _uiState.value = if (resultado.isSuccess) {
                AuthUiState.Exito
            } else {
                //AuthUiState.Error(resultado.exceptionOrNull()?.message ?: "Error desconocido")
                AuthUiState.Error(traducirError(resultado.exceptionOrNull()))
            }
        }
    }

    fun logout() {
        authRepository.logout()
        _uiState.value = AuthUiState.Idle
    }

    fun obtenerUsuarioActual() = authRepository.obtenerUsuarioActual()

    fun obtenerDatosUsuario(uid: String) {
        viewModelScope.launch {
            val resultado = authRepository.obtenerDatosUsuario(uid)
            if (resultado.isSuccess) {
                _usuario.value = resultado.getOrNull()
            }
        }
    }

    private fun traducirError(e: Throwable?): String{
        return "Correo o contraseña incorrectos, intenta de nuevo"
    }

    private val _usuario = MutableStateFlow<Usuario?>(null)
    val usuario: StateFlow<Usuario?> = _usuario.asStateFlow()
}

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Cargando : AuthUiState()
    object Exito : AuthUiState()
    data class Error(val mensaje: String) : AuthUiState()
}