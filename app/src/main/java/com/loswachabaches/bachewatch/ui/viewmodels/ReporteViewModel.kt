package com.loswachabaches.bachewatch.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loswachabaches.bachewatch.data.model.Reporte
import com.loswachabaches.bachewatch.data.repository.ReporteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReporteViewModel : ViewModel() {
    private val reporteRepository = ReporteRepository()

    private val _uiState = MutableStateFlow<ReporteUiState>(ReporteUiState.Idle)
    val uiState: StateFlow<ReporteUiState> = _uiState.asStateFlow()

    private val _reportes = MutableStateFlow<List<Reporte>>(emptyList())
    val reportes: StateFlow<List<Reporte>> = _reportes.asStateFlow()

    private val _misReportes = MutableStateFlow<List<Reporte>>(emptyList())
    val misReportes: StateFlow<List<Reporte>> = _misReportes.asStateFlow()

    private val _totalReportes = MutableStateFlow(0)
    val totalReportes: StateFlow<Int> = _totalReportes.asStateFlow()

    private val _reportesEnMiZona = MutableStateFlow<List<Reporte>>(emptyList())
    val reportesEnMiZona: StateFlow<List<Reporte>> = _reportesEnMiZona.asStateFlow()

    private val _zonasTop = MutableStateFlow<List<Pair<String, Int>>>(emptyList())
    val zonasTop: StateFlow<List<Pair<String, Int>>> = _zonasTop.asStateFlow()

    fun crearReporte(
        usuarioId: String,
        usuarioNombre: String,
        descripcion: String,
        latitud: Double,
        longitud: Double,
        direccionAproximada: String,
        fotoUri: Uri
    ) {
        viewModelScope.launch {
            _uiState.value = ReporteUiState.Cargando
            val resultado = reporteRepository.crearReporte(
                usuarioId, usuarioNombre, descripcion, latitud, longitud, direccionAproximada, fotoUri
            )
            _uiState.value = if (resultado.isSuccess) {
                ReporteUiState.Exito
            } else {
                ReporteUiState.Error(resultado.exceptionOrNull()?.message ?: "Error desconocido")
            }
        }
    }

    fun obtenerTodosLosReportes() {
        viewModelScope.launch {
            val resultado = reporteRepository.obtenerTodosLosReportes()
            if (resultado.isSuccess) {
                _reportes.value = resultado.getOrNull() ?: emptyList()
            }
        }
    }

    fun obtenerMisReportes(usuarioId: String) {
        viewModelScope.launch {
            val resultado = reporteRepository.obtenerReportesPorUsuario(usuarioId)
            if (resultado.isSuccess) {
                _misReportes.value = resultado.getOrNull() ?: emptyList()
            }
        }
    }

    fun contarMisReportes(usuarioId: String) {
        viewModelScope.launch {
            val resultado = reporteRepository.contarReportesPorUsuario(usuarioId)
            if (resultado.isSuccess) {
                _totalReportes.value = resultado.getOrNull() ?: 0
            }
        }
    }

    fun resetUiState() {
        _uiState.value = ReporteUiState.Idle
    }

    fun obtenerReportesCercanos(latitud: Double, longitud: Double) {
        viewModelScope.launch {
            val resultado = reporteRepository.obtenerReportesPorZona(latitud, longitud, radioKm = 2.0)
            if (resultado.isSuccess) {
                _reportesEnMiZona.value = resultado.getOrNull() ?: emptyList()
            }
        }
    }

    fun obtenerZonaConMasBaches() {
        viewModelScope.launch {
            val resultado = reporteRepository.obtenerZonaConMasBaches()
            if (resultado.isSuccess) {
                _zonasTop.value = resultado.getOrNull() ?: emptyList()
            }
        }
    }

    fun editarReporte(reporteId: String, nuevaDescripcion: String) {
        viewModelScope.launch {
            _uiState.value = ReporteUiState.Cargando
            val resultado = reporteRepository.editarReporte(reporteId, nuevaDescripcion)
            _uiState.value = if (resultado.isSuccess) ReporteUiState.Exito
            else ReporteUiState.Error(resultado.exceptionOrNull()?.message ?: "Error")
        }
    }

    fun eliminarReporte(reporteId: String) {
        viewModelScope.launch {
            _uiState.value = ReporteUiState.Cargando
            val resultado = reporteRepository.eliminarReporte(reporteId)
            _uiState.value = if (resultado.isSuccess) ReporteUiState.Exito
            else ReporteUiState.Error(resultado.exceptionOrNull()?.message ?: "Error")
        }
    }
}

sealed class ReporteUiState {
    object Idle : ReporteUiState()
    object Cargando : ReporteUiState()
    object Exito : ReporteUiState()
    data class Error(val mensaje: String) : ReporteUiState()
}