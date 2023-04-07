package com.example.storyapp.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.databinding.FragmentStoriesBinding
import com.example.storyapp.model.network.Story
import com.example.storyapp.repository.NetworkResult
import com.example.storyapp.view.adapter.StoriesAdapter
import com.example.storyapp.viewmodel.StoryViewModel
import com.example.storyapp.viewmodel.ViewModelFactory

class StoriesFragment : Fragment() {

    private lateinit var viewBinding : FragmentStoriesBinding
    private val sharedViewModel : StoryViewModel by activityViewModels{
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        viewBinding =  FragmentStoriesBinding.inflate(inflater, container, false)

        viewBinding.rvStories.layoutManager = LinearLayoutManager(requireActivity())

        sharedViewModel.getStories().observe(viewLifecycleOwner){ networkResult ->
            when(networkResult){
                is NetworkResult.Loading -> showLoading(true)
                is NetworkResult.Error -> {
                    showLoading(false)
                    Toast.makeText(requireActivity(), networkResult.message, Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Success -> {
                    showLoading(false)
                    Toast.makeText(requireActivity(), networkResult.data.message, Toast.LENGTH_SHORT).show()
                    bindDataToStoriesRecyclerView(networkResult.data.listStory)
                }
            }
        }

        return viewBinding.root
    }

    private fun bindDataToStoriesRecyclerView(stories : List<Story>){
        val storiesAdapter = StoriesAdapter()
        storiesAdapter.submitList(stories)
        viewBinding.rvStories.adapter = storiesAdapter
    }

    private fun showLoading(isLoading : Boolean){
        viewBinding.pbLoading.visibility = if(isLoading) View.VISIBLE else View.GONE
    }

}