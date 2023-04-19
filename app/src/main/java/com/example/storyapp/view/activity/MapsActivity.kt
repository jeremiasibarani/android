package com.example.storyapp.view.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityMapsBinding
import com.example.storyapp.model.network.GetAllStoriesResponse
import com.example.storyapp.model.network.Story
import com.example.storyapp.repository.NetworkResult
import com.example.storyapp.viewmodel.MapsViewModel
import com.example.storyapp.viewmodel.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap : GoogleMap
    private val viewModel : MapsViewModel by viewModels {
        ViewModelFactory.getInstance(applicationContext)
    }
    private val boundsBuilder = LatLngBounds.Builder()
    private lateinit var binding : ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel.getStories().observe(this@MapsActivity){networkResult ->
            when(networkResult){
                is NetworkResult.Loading -> showLoading(true)
                is NetworkResult.Error -> {
                    showLoading(false)
                    showToast(this@MapsActivity, networkResult.message)
                }
                is NetworkResult.Success -> {
                    showLoading(false)
                    networkResult.data.apply {
                        if(!error){
                            addMarkerFromStories(listStory)
                        }
                        showToast(this@MapsActivity, message)
                    }
                }
            }
        }

    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(
            MarkerOptions()
                .position(sydney)
                .title("Sydney")
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    private fun showToast(context : Context, message : String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading : Boolean){
        binding.pbLoading.visibility = if(isLoading) View.VISIBLE else View.GONE
    }

    private fun addMarkerFromStories(stories : List<Story>){
        stories.forEach {story ->
            val latLng = LatLng(story.lat, story.long)
            mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
            )
            boundsBuilder.include(latLng)
        }

        val bounds : LatLngBounds = boundsBuilder.build()
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                300
            )
        )
    }
}