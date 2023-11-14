package com.example.kaustudyroom.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class AuthRepository {
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun loginUser(email: String, password: String): Task<AuthResult> {
        return firebaseAuth.signInWithEmailAndPassword(email, password)
    }
}