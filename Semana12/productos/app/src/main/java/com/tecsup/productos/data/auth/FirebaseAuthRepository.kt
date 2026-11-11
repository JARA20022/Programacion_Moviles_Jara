package com.tecsup.productos.data.auth

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.tecsup.productos.domain.model.Rol
import com.tecsup.productos.domain.model.Sesion
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : AuthRepository {
    override suspend fun iniciarSesion(correo: String, clave: String): Result<Sesion> = try {
        val usuario = checkNotNull(auth.signInWithEmailAndPassword(correo, clave).await().user) {
            "No se pudo recuperar tu cuenta."
        }
        Result.success(leerSesion(usuario.uid, usuario.email ?: correo))
    } catch (error: CancellationException) {
        throw error
    } catch (error: Exception) {
        auth.signOut()
        Result.failure(IllegalStateException(mensajeDe(error), error))
    }

    override suspend fun sesionActual(): Sesion? {
        val usuario = auth.currentUser ?: return null
        return leerSesion(usuario.uid, usuario.email.orEmpty())
    }

    override fun cerrarSesion() {
        auth.signOut()
    }

    private suspend fun leerSesion(uid: String, correo: String): Sesion {
        val documento = db.collection("usuarios").document(uid).get().await()
        check(documento.exists()) { "Tu cuenta aún no tiene un perfil configurado." }

        val rol = when (documento.getString("rol")) {
            "admin" -> Rol.ADMIN
            "usuario" -> Rol.USUARIO
            else -> error("Tu cuenta aún no tiene un rol válido.")
        }
        return Sesion(uid = uid, correo = correo, rol = rol)
    }

    private fun mensajeDe(error: Exception): String = when (error) {
        is FirebaseAuthInvalidCredentialsException,
        is FirebaseAuthInvalidUserException -> "Correo o contraseña incorrectos."
        is FirebaseNetworkException -> "No se pudo conectar. Revisa tu conexión a internet."
        is FirebaseTooManyRequestsException -> "Demasiados intentos. Espera un momento y vuelve a intentar."
        is FirebaseFirestoreException -> "No se pudo consultar tu perfil. Revisa la configuración de tu cuenta."
        is IllegalStateException -> error.message ?: "Tu perfil está incompleto."
        else -> "No se pudo iniciar sesión. Inténtalo otra vez."
    }
}
