package com.loswachabaches.bachewatch.data.repository

import android.net.Uri
import java.util.UUID
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class StorageRepository {
    private val storage = FirebaseStorage.getInstance()

    suspend fun subirFoto(uri: Uri): Result<String> {
        return try {
            val ref = storage.reference
                .child("reportes/${UUID.randomUUID()}.jpg")

            ref.putFile(uri).await()
            val url = ref.downloadUrl.await().toString()
            Result.success(url)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}