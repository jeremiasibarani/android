package com.example.storyapp.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.storyapp.R

class StoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.story_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.setting -> {
                val intent = Intent(this@StoryActivity, SettingActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.location -> {
                val intent = Intent(this@StoryActivity, MapsActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return true
    }
}