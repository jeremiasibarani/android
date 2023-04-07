package com.example.storyapp.viewmodel


import androidx.lifecycle.ViewModel
import com.example.storyapp.repository.StoryRepository

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun getStories() = storyRepository.getStories()

}