package com.example.storyapp.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.databinding.FragmentDetailStoryBinding
import com.example.storyapp.model.network.Story
import com.example.storyapp.repository.NetworkResult
import com.example.storyapp.viewmodel.StoryViewModel
import com.example.storyapp.viewmodel.ViewModelFactory

class DetailStoryFragment : Fragment() {

    private lateinit var viewBinding : FragmentDetailStoryBinding
    private val sharedViewModel : StoryViewModel by activityViewModels{
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        viewBinding =  FragmentDetailStoryBinding.inflate(inflater, container, false)

        val storyId = arguments?.getString("story_id")
        storyId?.let{
            sharedViewModel.getDetailStory(it).observe(viewLifecycleOwner){ networkResult ->
                when(networkResult){
                    is NetworkResult.Loading -> showLoading(true)
                    is NetworkResult.Error -> {
                        showLoading(false)
                        Toast.makeText(requireActivity(), networkResult.message, Toast.LENGTH_SHORT).show()
                    }
                    is NetworkResult.Success -> {
                        showLoading(false)
                        networkResult.data.apply {
                            Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
                            if(!error){
                                bindDataToView(story)
                            }
                        }
                    }
                }
            }
        }

        return viewBinding.root
    }

    fun bindDataToView(story : Story){
        viewBinding.apply {
            tvName.text = story.name
            tvName.contentDescription = resources.getString(R.string.tv_user_name_detail_story_content_description, story.name)
            tvDescription.text = story.description
            tvDescription.contentDescription = story.description
            Glide.with(viewBinding.root.context)
                .load(story.photoUrl)
                .placeholder(R.drawable.placeholder)
                .into(imgAvatar)
        }
    }

    private fun showLoading(isLoading : Boolean){
        viewBinding.pbLoading.visibility = if(isLoading) View.VISIBLE else View.GONE
    }

}