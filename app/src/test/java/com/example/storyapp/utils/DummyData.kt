package com.example.storyapp.utils

import com.example.storyapp.model.local.StoryEntity

object DummyData {
    fun generateDummyStories() : List<StoryEntity>{
        val stories = mutableListOf<StoryEntity>()
        for(i in 0..100){
            stories.add(
                StoryEntity(
                    id = "${i + 1}",
                    name = "name $i",
                    description = "description $i",
                    photoUrl = "photoUrl $i",
                    createdAt = "createdAt $i",
                    latitude = i.toDouble(),
                    longitude = i.toDouble()
                )
            )
        }
        return stories
    }
}