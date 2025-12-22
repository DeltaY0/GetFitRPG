package com.example.getfitrpg.feature.auth

import android.util.Log
import com.example.getfitrpg.data.DatabaseManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthManager {

    private val tag = "AuthManager"
    private val auth: FirebaseAuth = Firebase.auth
    private val dbManager = DatabaseManager()

    fun createUser(username: String, email: String, password: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(tag, "createUserWithEmail:success")
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        dbManager.writeNewUser(userId, username, email)
                    }
                    sendEmailVerification()
                    onSuccess()
                } else {
                    Log.w(tag, "createUserWithEmail:failure", task.exception)
                    task.exception?.let { onFailure(it) }
                }
            }
    }

    fun signIn(email: String, password: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(tag, "signInWithEmail:success")
                    onSuccess()
                } else {
                    Log.w(tag, "signInWithEmail:failure", task.exception)
                    task.exception?.let { onFailure(it) }
                }
            }
    }

    fun sendPasswordResetEmail(email: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(tag, "sendPasswordResetEmail:success")
                    onSuccess()
                } else {
                    Log.w(tag, "sendPasswordResetEmail:failure", task.exception)
                    task.exception?.let { onFailure(it) }
                }
            }
    }

    fun verifyPasswordResetCode(code: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        auth.verifyPasswordResetCode(code)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(tag, "verifyPasswordResetCode:success")
                    onSuccess()
                } else {
                    Log.w(tag, "verifyPasswordResetCode:failure", task.exception)
                    task.exception?.let { onFailure(it) }
                }
            }
    }

    fun confirmPasswordReset(code: String, newPassword: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        auth.confirmPasswordReset(code, newPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(tag, "confirmPasswordReset:success")
                    onSuccess()
                } else {
                    Log.w(tag, "confirmPasswordReset:failure", task.exception)
                    task.exception?.let { onFailure(it) }
                }
            }
    }

    private fun sendEmailVerification() {
        val user = auth.currentUser
        user?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(tag, "Email sent.")
                }
            }
    }
}