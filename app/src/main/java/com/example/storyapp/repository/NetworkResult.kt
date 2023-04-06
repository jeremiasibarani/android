package com.example.storyapp.repository

sealed class NetworkResult<out R> private constructor(){
    data class Success<out T>(val data : T) : NetworkResult<T>()
    data class Error(val message : String, val code : Int? = null) : NetworkResult<Nothing>()
    object Loading : NetworkResult<Nothing>()
}