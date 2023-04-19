package com.example.storyapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.storyapp.databinding.StoriesLoadingBinding

class LoadingStateAdapter(
    private val retryAction : () -> Unit
) : LoadStateAdapter<LoadingStateAdapter.ViewHolder>() {

    inner class ViewHolder(
        private val binding : StoriesLoadingBinding,
        retryAction: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root){
        init{
            binding.retryButton.setOnClickListener {
                retryAction.invoke()
            }
        }
        fun bind(loadState: LoadState){
            binding.apply {
                if(loadState is LoadState.Error){
                    errorMsg.text = loadState.error.localizedMessage
                }
                progressBar.isVisible = loadState is LoadState.Loading
                retryButton.isVisible = loadState is LoadState.Error
                errorMsg.isVisible = loadState is LoadState.Error
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        return ViewHolder(
            StoriesLoadingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            retryAction
        )
    }

}