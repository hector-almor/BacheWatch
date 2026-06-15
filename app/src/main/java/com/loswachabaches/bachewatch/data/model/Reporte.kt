package com.loswachabaches.bachewatch.data.model

import java.util.Date

data class Reporte(
    val id: String = "",
    val uid: String = "",
    val usuarioId: String = "",
    val usuarioNombre: String = "",
    val descripcion: String = "",
    val latitud: Double = 0.0,
    val longitud: Double = 0.0,
    val direccionAproximada: String = "",
    val fotoUrl: String = "",
    val timestamp: Date = Date()
)