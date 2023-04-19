package com.example.storyapp.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.storyapp.model.local.RemoteKey
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
            val page = when(loadType){
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    nextKey
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    prevKey
                }
            }
            val response = storyApiService.getStoriesWithPagination(token, page, state.config.pageSize)
            val serverStories = response.body()?.listStory
            val endOfPaginationReached = serverStories.isNullOrEmpty()

            val localStories = serverStories?.map { story ->
                StoryEntity(
                    id = story.id,
                    name = story.name,
                    description = story.description,
                    photoUrl = story.photoUrl,
                    createdAt = story.createdAt,
                    latitude = story.lat,
                    longitude = story.long
                )
            } ?: emptyList()

            database.withTransaction {
                if(loadType  == LoadType.REFRESH){
                    database.remoteKeysDao().deleteAllRemoteKeys()
                    database.storyDao().deleteAllStories()
                }

                val prevKey = if(page == 1) null else page - 1
                val nextKey = if(endOfPaginationReached) null else page + 1
                val keys = localStories.map {storyEntity ->  
                    RemoteKey(
                        id = storyEntity.id,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
                database.remoteKeysDao().insertKeys(keys)
                database.storyDao().insertStories(localStories)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        }catch (e : Exception){
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, StoryEntity>): RemoteKey? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysById(data.id)
        }
    }
    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, StoryEntity>): RemoteKey? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysById(data.id)
        }
    }
    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, StoryEntity>): RemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteKeysDao().getRemoteKeysById(id)
            }
        }
    }

    private companion object{
        const val INITIAL_PAGE_INDEX = 1
    }

}