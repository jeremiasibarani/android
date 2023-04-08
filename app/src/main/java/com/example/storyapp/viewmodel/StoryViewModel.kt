package com.example.storyapp.viewmodel


import androidx.lifecycle.ViewModel
import com.example.storyapp.repository.StoryRepository
import java.io.File

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun getStories() = storyRepository.getStories()

    fun postStory(description : String, file : File) = storyRepository.addStory(description, file)

}