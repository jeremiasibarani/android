package com.example.storyapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.storyapp.repository.StoryRepository
import com.example.storyapp.util.Constants

class MapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun getStories() = storyRepository.getStories(Constants.LOCATION_REQUEST_TRUE)

}