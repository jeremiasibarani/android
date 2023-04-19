package com.example.storyapp.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.databinding.FragmentStoriesBinding
import com.example.storyapp.model.local.StoryEntity
import com.example.storyapp.model.network.Story
import com.example.storyapp.repository.NetworkResult
import com.example.storyapp.view.adapter.LoadingStateAdapter
import com.example.storyapp.view.adapter.StoriesAdapter
import com.example.storyapp.viewmodel.StoryViewModel
import com.example.storyapp.viewmodel.ViewModelFactory

class StoriesFragment : Fragment() {

    private lateinit var viewBinding : FragmentStoriesBinding
    private lateinit var storiesAdapter : StoriesAdapter
    private val sharedViewModel : StoryViewModel by activityViewModels{
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding =  FragmentStoriesBinding.inflate(inflater, container, false)
        setupPaginationAdapterForRecyclerView()
        viewBinding.fabAddPost.setOnClickListener {
            viewBinding.root.findNavController()
                .navigate(R.id.action_storiesFragment_to_addStoryFragment)
        }

        return viewBinding.root
    }

    private fun setupPaginationAdapterForRecyclerView(){
        storiesAdapter = StoriesAdapter()
        val loadingStateAdapter = LoadingStateAdapter{
            storiesAdapter.retry()
        }
        viewBinding.rvStories.adapter = storiesAdapter.withLoadStateFooter(
            footer = loadingStateAdapter
        )
        viewBinding.rvStories.layoutManager = LinearLayoutManager(requireActivity())
        storiesAdapter.addLoadStateListener { loadState ->
            if(loadState.refresh is LoadState.Loading){
                showLoading(true)
            }else{
                showLoading(false)
                if(loadState.append.endOfPaginationReached){
                    if(storiesAdapter.itemCount < 1){
                        showEmptyView(true)
                    }else{
                        showEmptyView(false)
                    }
                }
                val error = when{
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                error?.let{
                    Toast.makeText(requireActivity(), it.error.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        sharedViewModel.getStoriesWithPagination().observe(viewLifecycleOwner){pagingDataStory ->
            storiesAdapter.submitData(lifecycle, pagingDataStory)
        }
    }

    private fun showEmptyView(isDataEmpty : Boolean){
        viewBinding.apply {
            if(isDataEmpty){
                rvStories.visibility = View.GONE
                tvDataNotFound.visibility = View.VISIBLE
            }else{
                rvStories.visibility = View.VISIBLE
                tvDataNotFound.visibility = View.GONE
            }
        }
    }

    private fun showLoading(isLoading : Boolean){
        viewBinding.pbLoading.visibility = if(isLoading) View.VISIBLE else View.GONE
    }

}