package com.example.storyapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.di.Injector
import com.example.storyapp.repository.AuthRepository
import com.example.storyapp.repository.StoryRepository

@Suppress("UNCHECKED_CAST")
class ViewModelFactory private constructor(
    private val authRepository: AuthRepository? = null,
    private val storyRepository: StoryRepository? = null
) : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AuthViewModel::class.java)){
            return AuthViewModel(authRepository as AuthRepository) as T
        }else if(modelClass.isAssignableFrom(StoryViewModel::class.java)){
            return StoryViewModel(storyRepository as StoryRepository) as T
        }else if(modelClass.isAssignableFrom(MapsViewModel::class.java)){
            return MapsViewModel(storyRepository as StoryRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object{
        @Volatile
        private var instance : ViewModelFactory? = null
        fun getInstance(
            context : Context
        ) : ViewModelFactory = instance ?: synchronized(this){
                instance ?: ViewModelFactory(
                    authRepository = Injector.provideAuthRepository(context),
                    storyRepository = Injector.provideStoryRepository(context)
                )
        }.also {
            instance = it
        }
    }

}