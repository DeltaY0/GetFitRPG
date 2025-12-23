package com.example.getfitrpg.data

import com.google.firebase.database.database
import com.google.firebase.Firebase

class DatabaseManager {

    private val database = Firebase.database

    fun writeNewUser(userId: String, username: String, email: String) {
        val user = User(username, email)
        database.reference.child("users").child(userId).setValue(user)
    }

    fun updateUserWeight(userId: String, weight: Float) {
        database.reference.child("users").child(userId).child("weight").setValue(weight)
    }

    fun updateUserHeight(userId: String, height: Float) {
        database.reference.child("users").child(userId).child("height").setValue(height)
    }
}