package com.example.storyapp.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.storyapp.model.local.StoryDatabase
import com.example.storyapp.model.local.StoryEntity
import com.example.storyapp.model.network.StoryApiService

@OptIn(ExperimentalPagingApi::class)
class StoriesRemoteMediator(
    private val database : StoryDatabase,
    private val storyApiService: StoryApiService,
    private val token : String
) : RemoteMediator<Int, StoryEntity>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StoryEntity>
    ): MediatorResult {
        try{
            val page = INITIAL_PAGE_INDEX
            val response = storyApiService.getStoriesWithPagination(token, page, state.config.pageSize)
            val serverStories = response.body()?.listStory ?: emptyList()
            val endOfPaginationReached = serverStories.isEmpty()

            val localStories = serverStories.map { story ->
                StoryEntity(
                    id = story.id,
                    name = story.name,
                    description = story.description,
                    photoUrl = story.photoUrl,
                    createdAt = story.createdAt,
                    latitude = story.lat,
                    longitude = story.long
                )
            }

            database.withTransaction {
                if(loadType  == LoadType.REFRESH){
                    database.storyDao().deleteAllStories()
                }
                database.storyDao().insertStories(localStories)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        }catch (e : Exception){
            return MediatorResult.Error(e)
        }
    }

    private companion object{
        const val INITIAL_PAGE_INDEX = 1
    }

}