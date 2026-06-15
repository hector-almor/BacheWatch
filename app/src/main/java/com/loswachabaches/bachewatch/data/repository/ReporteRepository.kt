package com.loswachabaches.bachewatch.data.repository

import android.net.Uri
import com.loswachabaches.bachewatch.data.model.Reporte
import java.util.Date
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class ReporteRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val storageRepository = StorageRepository()

    suspend fun crearReporte(
        usuarioId: String,
        usuarioNombre: String,
        descripcion: String,
        latitud: Double,
        longitud: Double,
        direccionAproximada: String,
        fotoUri: Uri
    ): Result<Unit> {
        return try {
            // 1. Subir foto primero
            val fotoUrl = storageRepository.subirFoto(fotoUri).getOrThrow()

            // 2. Crear reporte con la URL de la foto
            val docRef = firestore.collection("reportes").document()
            val reporte = Reporte(
                id = docRef.id,
                usuarioId = usuarioId,
                usuarioNombre = usuarioNombre,
                descripcion = descripcion,
                latitud = latitud,
                longitud = longitud,
                direccionAproximada = direccionAproximada,
                fotoUrl = fotoUrl,
                timestamp = Date()
            )

            docRef.set(reporte).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun obtenerTodosLosReportes(): Result<List<Reporte>> {
        return try {
            val snapshot = firestore.collection("reportes")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get().await()

            val reportes = snapshot.toObjects(Reporte::class.java)
            Result.success(reportes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun obtenerReportesPorUsuario(usuarioId: String): Result<List<Reporte>> {
        return try {
            val snapshot = firestore.collection("reportes")
                .whereEqualTo("usuarioId", usuarioId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get().await()

            val reportes = snapshot.toObjects(Reporte::class.java)
            Result.success(reportes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun contarReportesPorUsuario(usuarioId: String): Result<Int> {
        return try {
            val snapshot = firestore.collection("reportes")
                .whereEqualTo("usuarioId", usuarioId)
                .get().await()

            Result.success(snapshot.size())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}