package com.loswachabaches.bachewatch.ui.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class EstadisticasViewModel : ViewModel() {

    private val _ubicacionActual = MutableStateFlow<Location?>(null)
    val ubicacionActual: StateFlow<Location?> = _ubicacionActual.asStateFlow()

    @SuppressLint("MissingPermission")
    fun obtenerUbicacion(context: Context, onObtenida: (Double, Double) -> Unit) {
        viewModelScope.launch {
            try {
                val client = LocationServices.getFusedLocationProviderClient(context)
                val location = client.lastLocation.await()
                location?.let {
                    _ubicacionActual.value = it
                    onObtenida(it.latitude, it.longitude)
                }
            } catch (e: Exception) {
                // Si falla, usar CDMX como fallback
                onObtenida(19.4326, -99.1332)
            }
        }
    }
}