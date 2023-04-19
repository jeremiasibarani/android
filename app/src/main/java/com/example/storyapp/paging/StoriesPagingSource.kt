package com.example.storyapp.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storyapp.model.network.Story
import com.example.storyapp.model.network.StoryApiService

class StoriesPagingSource(
    private val storyApiService: StoryApiService,
    private val token : String
) : PagingSource<Int, Story>() {
    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try{
            val position = params.key ?: INITIAL_PAGE_INDEX
            val response = storyApiService.getStoriesWithPagination(
                token, position, params.loadSize
            )
            val responseData = response.body()?.listStory ?: emptyList()
            LoadResult.Page(
                data = responseData,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.isEmpty()) null else position + 1
            )
        }catch (e : Exception){
            LoadResult.Error(e)
        }
    }

    private companion object{
        const val INITIAL_PAGE_INDEX = 1
    }

}