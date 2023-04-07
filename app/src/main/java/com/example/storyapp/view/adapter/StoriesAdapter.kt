package com.example.storyapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.databinding.StoryItemBinding
import com.example.storyapp.model.network.Story

class StoriesAdapter : ListAdapter<Story, StoriesAdapter.ViewHolder>(StoryItemCallback) {

    inner class ViewHolder(private val binding : StoryItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(story : Story){
            binding.apply {
                tvDescription.text = story.description
                tvName.text = story.name
            }
            Glide.with(binding.root.context)
                .load(story.photoUrl)
                .placeholder(R.drawable.placeholder)
                .into(binding.imgAvatar)
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
