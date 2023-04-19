package com.example.storyapp.model.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StoryDao{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStories(stories : List<StoryEntity>)

    @Query("SELECT * FROM stories")
    fun getStories() : PagingSource<Int, StoryEntity>

    @Query("DELETE FROM stories")
    suspend fun deleteAllStories()

}