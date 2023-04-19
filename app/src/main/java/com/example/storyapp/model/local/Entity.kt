package com.example.storyapp.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stories")
data class StoryEntity(
    @PrimaryKey
    val id : String,
    val name : String,
    val description : String,
    val photoUrl : String?,
    val createdAt : String,
    val latitude : Double,
    val longitude : Double
)

@Entity(tableName = "remote_keys")
data class RemoteKey(
    @PrimaryKey
    val id : String,
    val prevKey : Int?,
    val nextKey : Int?
)