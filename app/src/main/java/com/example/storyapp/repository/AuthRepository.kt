package com.example.storyapp.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.example.storyapp.datastore.AuthPreferences
import com.example.storyapp.model.network.AuthApiService
import com.example.storyapp.model.network.LoginResponse
import com.example.storyapp.model.network.RegisterResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AuthRepository(
    private val authApiService: AuthApiService,
    private val authPreferences: AuthPreferences
) {

    fun login(email : String, password : String) : LiveData<NetworkResult<LoginResponse>> = liveData {
        emit(NetworkResult.Loading)
        try{
            val response = authApiService.login(email, password)
            val responseBody = response.body()
            if(response.isSuccessful && responseBody != null){
                emit(NetworkResult.Success(responseBody))
                val token = responseBody.loginResult.token
                authPreferences.saveToken(responseBody.loginResult.token)
            }else{
                emit(NetworkResult.Error(response.message(), response.code()))
            }
        }catch (e : Exception){
            emit(NetworkResult.Error(e.message.toString()))
        }
    }

    fun register(name : String, email : String, password : String) : LiveData<NetworkResult<RegisterResponse>> = liveData {
        emit(NetworkResult.Loading)
        try{
            val response = authApiService.register(name, email, password)
            val responseBody = response.body()
            if(response.isSuccessful && responseBody != null){
                emit(NetworkResult.Success(responseBody))
            }else{
                emit(NetworkResult.Error(response.message(), response.code()))
            }
        }catch (e : Exception){
            emit(NetworkResult.Error(e.message.toString()))
        }
    }

    fun getToken() : LiveData<String> = authPreferences.getToken().asLiveData()

}