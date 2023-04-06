package com.example.storyapp.model.network

data class RegisterResponse(
    val error : String,
    val message : String
)

data class LoginResponse(
    val error : String,
    val message : String,
    val loginResult : LoginResult
)

data class LoginResult(
    val userId : String,
    val name : String,
    val token : String
)