package com.loswachabaches.bachewatch.data.repository

import com.loswachabaches.bachewatch.data.model.Usuario
import java.util.Date
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun registrar(nombre: String, correo: String, contrasena: String): Result<Unit> {
        return try {
            val resultado = auth.createUserWithEmailAndPassword(correo, contrasena).await()
            val uid = resultado.user!!.uid

            val usuario = Usuario(
                uid = uid,
                nombre = nombre,
                correo = correo,
                fechaRegistro = Date()
            )

            firestore.collection("usuarios").document(uid).set(usuario).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(correo: String, contrasena: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(correo, contrasena).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() = auth.signOut()

    fun obtenerUsuarioActual() = auth.currentUser

    suspend fun obtenerDatosUsuario(uid: String): Result<Usuario> {
        return try {
            val doc = firestore.collection("usuarios").document(uid).get().await()
            val usuario = doc.toObject(Usuario::class.java)!!
            Result.success(usuario)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}