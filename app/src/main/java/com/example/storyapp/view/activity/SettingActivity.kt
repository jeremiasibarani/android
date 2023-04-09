package com.example.storyapp.view.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
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
        playAnimation()
    }

    private fun playAnimation(){
        val avatar = ObjectAnimator.ofFloat(viewBinding.cvAvatar, View.ALPHA, 1f).setDuration(500)
        val name = ObjectAnimator.ofFloat(viewBinding.tvName, View.ALPHA, 1f).setDuration(500)
        val language = ObjectAnimator.ofFloat(viewBinding.cvLanguageSetting, View.ALPHA, 1f).setDuration(500)
        val logout = ObjectAnimator.ofFloat(viewBinding.cvLogout, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(avatar, name, language, logout)
            start()
        }
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
                    val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                    startActivity(intent)
                }
            }
            cvLogout.children.forEach {
                it.setOnClickListener {
                    viewModel.clearAllPreferences()
                    val intent = Intent(this@SettingActivity, MainActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
            }
        }
    }
}