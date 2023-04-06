package com.example.storyapp.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.storyapp.R
import com.example.storyapp.databinding.FragmentStoriesBinding

class StoriesFragment : Fragment() {

    private lateinit var viewBinding : FragmentStoriesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        viewBinding =  FragmentStoriesBinding.inflate(inflater, container, false)

        return viewBinding.root
    }

}