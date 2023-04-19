package com.example.storyapp.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.storyapp.repository.StoryRepository
import com.example.storyapp.util.Constants
import java.io.File

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun getStories() = storyRepository.getStories()
    fun getStoriesWithPagination() = storyRepository.getStoriesWithPagination(Constants.DEFAULT_PAGE_SIZE_STORY_REQUEST)
        .cachedIn(viewModelScope)
    fun postStory(description : String, file : File) = storyRepository.addStory(description, file)
    fun getDetailStory(storyId : String) = storyRepository.getDetailStory(storyId)

}