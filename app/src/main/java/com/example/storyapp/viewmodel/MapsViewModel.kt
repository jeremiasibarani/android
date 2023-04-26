package com.example.storyapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.storyapp.repository.StoryRepository
import com.example.storyapp.util.Constants.LOCATION_REQUEST_TRUE

class MapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun getStories(
        location : Int = LOCATION_REQUEST_TRUE,
        token : String
    ) = storyRepository.getStories(location, token)

    fun getToken() = storyRepository.getToken()

}