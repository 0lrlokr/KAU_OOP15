package com.example.kaustudyroom.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kaustudyroom.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import java.sql.RowId

class AuthViewModel: ViewModel() {
    private val _loginStatus = MutableLiveData<Boolean>()
    val loginStatus: LiveData<Boolean> = _loginStatus



    private val repository = AuthRepository()
    fun loginUser(email: String, password: String) {
        repository.loginUser(email, password)
            .addOnCompleteListener { task ->
                _loginStatus.value = task.isSuccessful
            }
    }

    fun getUserIdDirectly(): String {
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        return user?.uid ?: ""

        Log.d("getUserIdDirectly로 가져오기","${user?.uid}")
    }
}