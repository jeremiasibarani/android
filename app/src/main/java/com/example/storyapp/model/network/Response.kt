package com.example.storyapp.model.network

data class RegisterResponse(
    val error : Boolean,
    val message : String
)

data class LoginResponse(
    val error : Boolean,
    val message : String,
    val loginResult : LoginResult
)

data class LoginResult(
    val userId : String,
    val name : String,
    val token : String
)

data class GetAllStoriesResponse(
    val error : Boolean,
    val message : String,
    val listStory : List<Story>
)

data class Story(
    val id : String,
    val name : String,
    val description : String,
    val photoUrl : String?,
    val createdAt : String,
    val lat : Double,
    val lon : Double
)

data class AddStoryResponse(
    val error : Boolean,
    val message : String
)

data class DetailStoryResponse(
    val error : Boolean,
    val message : String,
    val story : Story
)