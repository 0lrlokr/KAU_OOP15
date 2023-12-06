package com.example.kaustudyroom.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kaustudyroom.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth

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
        // usreId : uid 가져오는 함수
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        return user?.uid ?: ""
    }
}