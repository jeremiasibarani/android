package com.example.storyapp.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.children
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivitySettingBinding
import com.example.storyapp.viewmodel.AuthViewModel
import com.example.storyapp.viewmodel.ViewModelFactory

class SettingActivity : AppCompatActivity() {

    private lateinit var viewBinding : ActivitySettingBinding
    private val viewModel : AuthViewModel by viewModels{
        ViewModelFactory.getInstance(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        setupView()
    }

    private fun setupView(){
        viewModel.getName().observe(this@SettingActivity){ name ->
            viewBinding.tvName.apply {
                contentDescription = resources.getString(R.string.tv_user_name_detail_story_content_description, name)
                text = name
            }
        }

        viewBinding.apply {
            cvLanguageSetting.children.forEach {
                it.setOnClickListener {
                    // Todo(Language dialog setting here)
                }
            }
            cvLogout.children.forEach {
                it.setOnClickListener {
                    viewModel.clearAllPrefences()
                    val intent = Intent(this@SettingActivity, MainActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
            }
        }
    }
}