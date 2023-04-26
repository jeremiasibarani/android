package com.example.storyapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.example.storyapp.datastore.AuthPreferences
import com.example.storyapp.model.local.StoryDatabase
import com.example.storyapp.model.local.StoryEntity
import com.example.storyapp.model.network.*
import com.example.storyapp.paging.StoriesRemoteMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import com.example.storyapp.util.Constants.LOCATION_REQUEST_FALSE
import okhttp3.RequestBody


class StoryRepository(
    private val storyApiService: StoryApiService,
    private val authPreferences: AuthPreferences,
    private val storyDatabase: StoryDatabase
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
    @OptIn(ExperimentalPagingApi::class)
    fun getStoriesWithPagination(
        pageSize : Int
    ) : LiveData<PagingData<StoryEntity>> = Pager(
        config = PagingConfig(
            pageSize = pageSize
        ),
        remoteMediator = StoriesRemoteMediator(
            storyDatabase, storyApiService, "Bearer $token"
        ),
        pagingSourceFactory = {
            storyDatabase.storyDao().getStories()
        }
    ).liveData

    fun getStories(location : Int = LOCATION_REQUEST_FALSE) : LiveData<NetworkResult<GetAllStoriesResponse>> = liveData{
        emit(NetworkResult.Loading)
        try{
            Log.i(TAG, token)
            val response = storyApiService.getStories("Bearer $token", location)
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

    fun addStory(description : String, file : File, lat : Double?, lot : Double?) : LiveData<NetworkResult<AddStoryResponse>> = liveData{
        emit(NetworkResult.Loading)
        val parts = mutableMapOf<String, RequestBody>()
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultiPart : MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )
        val descriptionRequestBody = description.toRequestBody("text/plain".toMediaType())
        parts["description"] = descriptionRequestBody
        if(lat != null && lot != null){
            val latRequestBody = lat.toString().toRequestBody("text/plain".toMediaType())
            val lotRequestBody = lot.toString().toRequestBody("text/plain".toMediaType())
            parts["lat"] = latRequestBody
            parts["lon"] = lotRequestBody
        }

        try{
            val response = storyApiService.addStory(
                token = "Bearer $token",
                parts = parts,
                file = imageMultiPart
            )
            val responseBody = response.body()
            if(response.isSuccessful && responseBody != null){
                emit(NetworkResult.Success(responseBody))
            }else{
                emit(NetworkResult.Error(response.message(), response.code()))
            }

        }catch (e : Exception){
            emit(NetworkResult.Error(e.message.toString()))
            Log.e(TAG, e.message, e)
        }
    }

    fun getDetailStory(storyId : String) : LiveData<NetworkResult<DetailStoryResponse>> = liveData {
        emit(NetworkResult.Loading)
        try{
            Log.i(TAG, token)
            val response = storyApiService.getDetailStory("Bearer $token", storyId)
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