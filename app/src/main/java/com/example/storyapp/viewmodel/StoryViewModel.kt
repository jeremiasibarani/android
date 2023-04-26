package com.example.storyapp.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.storyapp.repository.StoryRepository
import com.example.storyapp.util.Constants.DEFAULT_PAGE_SIZE_STORY_REQUEST
import java.io.File

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getStoriesWithPagination(
        pageSize : Int = DEFAULT_PAGE_SIZE_STORY_REQUEST,
        token : String
    ) = storyRepository.getStoriesWithPagination(pageSize, token)
        .cachedIn(viewModelScope)
    fun postStory(
        description : String,
        file : File,
        lat : Double?,
        lot : Double?,
        token : String
    ) = storyRepository.addStory(description, file, lat, lot, token)
    fun getDetailStory(storyId : String, token : String) = storyRepository.getDetailStory(storyId, token)

    fun getToken() = storyRepository.getToken()

}