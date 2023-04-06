package com.example.storyapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.di.Injector
import com.example.storyapp.model.network.AuthApiService
import com.example.storyapp.repository.AuthRepository

@Suppress("UNCHECKED_CAST")
class ViewModelFactory private constructor(
    private val authRepository: AuthRepository? = null
) : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AuthViewModel::class.java)){
            return AuthViewModel(authRepository as AuthRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object{
        @Volatile
        private var instance : ViewModelFactory? = null
        fun getInstance(
            context : Context
        ) : ViewModelFactory = instance ?: synchronized(this){
                instance ?: ViewModelFactory(Injector.provideAuthRepository(context))
        }.also {
            instance = it
        }
    }

}