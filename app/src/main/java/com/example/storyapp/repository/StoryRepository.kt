package com.example.storyapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.storyapp.datastore.AuthPreferences
import com.example.storyapp.model.network.GetAllStoriesResponse
import com.example.storyapp.model.network.StoryApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class StoryRepository(
    private val storyApiService: StoryApiService,
    private val authPreferences: AuthPreferences
) {

    private lateinit var token : String
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    init{
        coroutineScope.launch {
            authPreferences.getToken().collect{
                token = it
            }
        }
    }

    fun getStories() : LiveData<NetworkResult<GetAllStoriesResponse>> = liveData{
        emit(NetworkResult.Loading)
        try{
            Log.i(TAG, token)
            val response = storyApiService.getStories("Bearer $token")
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

    companion object{
        private val TAG = StoryRepository::class.java.simpleName
    }

}