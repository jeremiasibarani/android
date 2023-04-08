package com.example.storyapp.model.network

import com.example.storyapp.BuildConfig
import com.example.storyapp.util.Constants
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AuthApiService {

    @FormUrlEncoded
    @POST(Constants.REGISTER_PATH)
    suspend fun register(
        @Field("name") name : String,
        @Field("email") email : String,
        @Field("password") password : String
    ) : Response<RegisterResponse>

    @FormUrlEncoded
    @POST(Constants.LOGIN_PATH)
    suspend fun login(
        @Field("email") email : String,
        @Field("password") password: String
    ) : Response<LoginResponse>

}

interface StoryApiService{

    @GET(Constants.STORIES_PATH)
    suspend fun getStories(@Header("Authorization") token : String) : Response<GetAllStoriesResponse>

    @Multipart
    @POST(Constants.STORIES_PATH)
    suspend fun addStory(
        @Header("Authorization") token : String,
        @Part("description") description : RequestBody,
        @Part file : MultipartBody.Part
    ) : Response<AddStoryResponse>

}

class ApiConfig{
    companion object{
        private val loggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        private val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        private val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_API)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        fun getAuthApiService() : AuthApiService{
            return retrofit.create(AuthApiService::class.java)
        }
        fun getStoryApiService() : StoryApiService{
            return retrofit.create(StoryApiService::class.java)
        }
    }
}