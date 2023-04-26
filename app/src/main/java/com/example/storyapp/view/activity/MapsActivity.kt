package com.example.storyapp.view.activity

import android.content.Context
import android.content.res.Resources
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityMapsBinding
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
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.*

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

        supportActionBar?.hide()

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
        mMap.uiSettings.apply {
            isZoomControlsEnabled = true
            isCompassEnabled = true
            isMapToolbarEnabled = true
        }
        setupMapStyle()

    }

    private fun setupMapStyle(){
        try{
            val success = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this@MapsActivity, R.raw.map_style))
            if(!success){
                Log.e(TAG, "Style parsing failed")
            }
        }catch (e : Resources.NotFoundException){
            Log.e(TAG, e.message, e)
        }
    }

    private fun getAddressName(lat : Double, lon : Double) : String?{
        var addressName : String? = null
        val geocoder = Geocoder(this@MapsActivity, Locale.getDefault())
        try {
            @Suppress("DEPRECATION")
            val list = geocoder.getFromLocation(lat, lon, 1)
            if (list != null && list.size != 0) {
                addressName = list[0].getAddressLine(0)
            }
        }catch (e : IOException){
            Log.e(TAG, e.message, e)
        }
        return addressName
    }

    private fun showToast(context : Context, message : String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading : Boolean){
        binding.pbLoading.visibility = if(isLoading) View.VISIBLE else View.GONE
    }

    private fun addMarkerFromStories(stories : List<Story>){
        stories.forEach {story ->
            val latLng = LatLng(story.lat, story.lon)
            val addressName = getAddressName(story.lat, story.lon)
            mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(story.name)
                    .snippet(addressName)
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

    companion object{
        private val TAG = MapsActivity::class.java.simpleName
    }

}