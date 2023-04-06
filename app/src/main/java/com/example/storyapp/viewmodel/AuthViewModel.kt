package com.example.storyapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.storyapp.repository.AuthRepository

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun login(email : String, password : String) = authRepository.login(email, password)
    fun register(name : String, email : String, password: String) = authRepository.register(name, email, password)


}