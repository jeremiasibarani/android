package com.example.storyapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.databinding.StoryItemBinding
import com.example.storyapp.model.network.Story
import com.example.storyapp.view.fragment.StoriesFragmentDirections

class StoriesAdapter : ListAdapter<Story, StoriesAdapter.ViewHolder>(StoryItemCallback) {

    inner class ViewHolder(private val binding : StoryItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(story : Story){
            binding.apply {
                tvDescription.text = story.description
                tvName.text = story.name
                tvName.contentDescription = root.resources.getString(R.string.tv_user_name_detail_story_content_description, story.name)
            }
            Glide.with(binding.root.context)
                .load(story.photoUrl)
                .placeholder(R.drawable.placeholder)
                .into(binding.imgAvatar)

            binding.root.setOnClickListener {
                val action = StoriesFragmentDirections.actionStoriesFragmentToDetailStoryFragment(story.id)
                it.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            StoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object{
        private val StoryItemCallback = object : DiffUtil.ItemCallback<Story>(){
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean = oldItem == newItem
        }
    }

}
