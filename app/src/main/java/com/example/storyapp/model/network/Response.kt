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