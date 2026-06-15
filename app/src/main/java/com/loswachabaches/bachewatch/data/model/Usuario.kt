package com.loswachabaches.bachewatch.data.model

import java.util.Date

data class Usuario(
    val uid: String = "",
    val nombre: String = "",
    val correo: String = "",
    val fechaRegistro: Date = Date()
)
