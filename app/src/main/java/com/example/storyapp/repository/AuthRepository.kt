package com.example.storyapp.repository


import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.example.storyapp.datastore.AuthPreferences
import com.example.storyapp.model.network.AuthApiService
import com.example.storyapp.model.network.LoginResponse
import com.example.storyapp.model.network.RegisterResponse
import com.example.storyapp.util.wrapEspressoIdlingResource


class AuthRepository(
    private val authApiService: AuthApiService,
    private val authPreferences: AuthPreferences
) {

    fun login(email : String, password : String) : LiveData<NetworkResult<LoginResponse>> = liveData {
        emit(NetworkResult.Loading)
        wrapEspressoIdlingResource {
            try{
                val response = authApiService.login(email, password)
                val responseBody = response.body()
                if(response.isSuccessful && responseBody != null){
                    emit(NetworkResult.Success(responseBody))
                    authPreferences.saveToken(responseBody.loginResult.token)
                    authPreferences.saveName(responseBody.loginResult.name)
                }else{
                    emit(NetworkResult.Error(response.message(), response.code()))
                }
            }catch (e : Exception){
                emit(NetworkResult.Error(e.message.toString()))
            }
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

    fun getName() : LiveData<String> = authPreferences.getName().asLiveData()

    suspend fun clearPreferences() = authPreferences.clearAllPreferences()

}