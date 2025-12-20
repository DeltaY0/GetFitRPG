package com.example.getfitrpg.data

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DatabaseManager {

    private val database = Firebase.database

    fun writeNewUser(userId: String, username: String, email: String) {
        val user = User(username, email)
        database.reference.child("users").child(userId).setValue(user)
    }
}