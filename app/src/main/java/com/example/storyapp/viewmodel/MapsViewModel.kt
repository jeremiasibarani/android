package com.example.storyapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.storyapp.repository.StoryRepository
import com.example.storyapp.util.Constants.LOCATION_REQUEST_TRUE

class MapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun getStories() = storyRepository.getStories(LOCATION_REQUEST_TRUE)

}