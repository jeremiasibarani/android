package com.example.storyapp.viewmodel

import android.media.session.MediaSession.Token
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun login(email : String, password : String) = authRepository.login(email, password)
    fun register(name : String, email : String, password: String) = authRepository.register(name, email, password)

    fun getToken() : LiveData<String> = authRepository.getToken()

    fun getName() : LiveData<String> = authRepository.getName()
    fun clearAllPrefences() = viewModelScope.launch {
        authRepository.clearPreferences()
    }

}