package com.example.storyapp.util

fun isEmailValid(email : String) : Boolean{
    val emailPattern = "[a-zA-Z\\d._-]+@[a-z]+\\.+[a-z]+".toRegex()
    return email.matches(emailPattern)
}