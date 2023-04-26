package com.example.storyapp.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.storyapp.R

class AuthenticationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
    }
}