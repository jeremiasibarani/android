package com.example.storyapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.storyapp.model.local.StoryEntity
import com.example.storyapp.repository.StoryRepository
import com.example.storyapp.utils.DummyData
import com.example.storyapp.utils.MainDispatcherRule
import com.example.storyapp.utils.getOrAwaitValue
import com.example.storyapp.view.adapter.StoriesAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest{
    @get:Rule
    val instantTaskExecutor = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var storyViewModel: StoryViewModel
    private val dummyStories = DummyData.generateDummyStories()

    @Mock
    private lateinit var storyRepository: StoryRepository

    @Test
    fun `get stories shouldn't be null`() = runTest {
        val data : PagingData<StoryEntity> = StoriesPagingSource.snapshot(dummyStories)
        val expectedStories = MutableLiveData<PagingData<StoryEntity>>()
        expectedStories.value = data
        `when`(storyRepository.getStoriesWithPagination(5)).thenReturn(expectedStories)

        storyViewModel = StoryViewModel(storyRepository)
        val actualStories : PagingData<StoryEntity> = storyRepository.getStoriesWithPagination(5).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoriesAdapter.StoryItemCallback,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualStories)

        val differData = differ.snapshot()
        assertNotNull(differData)
        assertEquals(dummyStories.size, differData.size)
        assertEquals(dummyStories[0], differData[0])
    }

    @Test
    fun `get stories should return empty list`() = runTest {
        val data : PagingData<StoryEntity> = StoriesPagingSource.snapshot(emptyList())
        val expectedStories = MutableLiveData<PagingData<StoryEntity>>()
        expectedStories.value = data
        `when`(storyRepository.getStoriesWithPagination(5)).thenReturn(expectedStories)

        storyViewModel = StoryViewModel(storyRepository)
        val actualStories : PagingData<StoryEntity> = storyRepository.getStoriesWithPagination(5).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoriesAdapter.StoryItemCallback,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualStories)

        val differData = differ.snapshot()
        assertEquals(differData.size, 0)

    }

    class StoriesPagingSource : PagingSource<Int, LiveData<List<StoryEntity>>>(){
        companion object{
            fun snapshot(stories : List<StoryEntity>) : PagingData<StoryEntity>{
                return PagingData.from(stories)
            }
        }

        override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryEntity>>>): Int {
            return 0
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryEntity>>> {
            return LoadResult.Page(emptyList(), 0, 1)
        }
    }

}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}