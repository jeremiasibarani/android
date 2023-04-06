package com.example.storyapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.storyapp.datastore.AuthPreferences
import com.example.storyapp.model.network.ApiConfig
import com.example.storyapp.repository.AuthRepository

val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "authentication")

object Injector {
    //fun provideAuthRepository(authApiService: AuthApiService) : AuthRepository = AuthRepository(authApiService)
    fun provideAuthRepository(context : Context) : AuthRepository{
        val authApiService = ApiConfig.getAuthApiService()
        val authPreferences = AuthPreferences(context.dataStore)
        return AuthRepository(authApiService, authPreferences)
    }
}