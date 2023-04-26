package com.example.storyapp.view.fragment

import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.storyapp.R
import com.example.storyapp.databinding.FragmentAddStoryBinding
import com.example.storyapp.repository.NetworkResult
import com.example.storyapp.util.createTempImageFile
import com.example.storyapp.util.reduceFileImage
import com.example.storyapp.util.rotateBitmap
import com.example.storyapp.util.uriToFile
import com.example.storyapp.viewmodel.StoryViewModel
import com.example.storyapp.viewmodel.ViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.File

class AddStoryFragment : Fragment() {

    private lateinit var viewBinding : FragmentAddStoryBinding
    private var getFile : File? = null
    private lateinit var currentPhotoPath : String
    private val sharedViewModel : StoryViewModel by activityViewModels{
        ViewModelFactory.getInstance(requireContext())
    }
    private lateinit var fusedLocationClient : FusedLocationProviderClient
    private var lat : Double? = null
    private var long : Double? = null

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ){permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
            getMyLastLocation()
        }
        else if (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            getMyLastLocation()
        }
        else if(permissions[Manifest.permission.CAMERA] == true) {
            startTakePhoto()
        }
        else{
            showToast(requireContext(), resources.getString(R.string.permission_not_granted))
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ result ->
        if(result.resultCode == RESULT_OK){
            getFile = File(currentPhotoPath)
            getFile?.let{
                val resultBitmap = BitmapFactory.decodeFile(it.path)
                viewBinding.imgPreview.setImageBitmap(resultBitmap)
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ result ->
        if(result.resultCode == RESULT_OK){
            val selectedImg : Uri = result.data?.data as Uri
            getFile = uriToFile(selectedImg, requireActivity())
            viewBinding.imgPreview.setImageURI(selectedImg)
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        viewBinding =  FragmentAddStoryBinding.inflate(inflater, container, false)

        viewBinding.apply {
            btnUploadFromCamera.setOnClickListener {
                startTakePhoto()
            }

            btnUploadFromGallery.setOnClickListener {
                startGallery()
            }

            btnPostStory.setOnClickListener {
                uploadStory()
            }
            cbIncludeLocation.setOnCheckedChangeListener{_, isChecked ->
                if(isChecked){
                    getMyLastLocation()
                }else{
                    lat = null
                    long = null
                }
            }
        }

        playAnimation()

        return viewBinding.root
    }

    @SuppressLint("MissingPermission")
    private fun getMyLastLocation(){
        if(checkPermissions(LOCATION_PERMISSIONS)){
            if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q){
                val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                }
            }
            fusedLocationClient.lastLocation.addOnSuccessListener {location ->
                if(location != null){
                    lat = location.latitude
                    long = location.longitude
                }else{
                    viewBinding.cbIncludeLocation.isChecked = false
                    showToast(requireActivity(), resources.getString(R.string.location_not_found))
                }
            }
        }else{
            requestPermissionLauncher.launch(LOCATION_PERMISSIONS)
        }
    }

    private fun showToast(context : Context, message : String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun checkPermissions(permissions : Array<String>) : Boolean = permissions.all{permission ->
        ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun uploadStory(){
        val description = viewBinding.etDescription.text.toString()
        if(getFile != null && description.isNotEmpty()){
            val uploadedFile : File = reduceFileImage(getFile!!)
            sharedViewModel.postStory(description, uploadedFile, lat, long).observe(viewLifecycleOwner){ networkResult ->
                when(networkResult){
                    is NetworkResult.Loading -> showLoading(true)
                    is NetworkResult.Error -> {
                        showLoading(false)
                        showToast(requireActivity(), networkResult.message)
                    }
                    is NetworkResult.Success -> {
                        showLoading(false)
                        networkResult.data.apply {
                            if(!error){
                                showToast(requireActivity(), message)
                                viewBinding.root.findNavController()
                                    .navigate(R.id.action_addStoryFragment_to_storiesFragment)
                            }
                        }
                    }
                }
            }
        }else{
            showToast(requireActivity(), resources.getString(R.string.post_description_or_image_empty))
        }

    }

    private fun playAnimation(){
        val avatar = ObjectAnimator.ofFloat(viewBinding.cvAvatar, View.ALPHA, 1f).setDuration(500)
        val camera = ObjectAnimator.ofFloat(viewBinding.btnUploadFromCamera, View.ALPHA, 1f).setDuration(500)
        val gallery = ObjectAnimator.ofFloat(viewBinding.btnUploadFromGallery, View.ALPHA, 1f).setDuration(500)
        val description = ObjectAnimator.ofFloat(viewBinding.etDescription, View.ALPHA, 1f).setDuration(500)
        val upload = ObjectAnimator.ofFloat(viewBinding.btnPostStory, View.ALPHA, 1f).setDuration(500)


        val cameraGallery = AnimatorSet().apply {
            playTogether(camera, gallery)
        }

        AnimatorSet().apply {
            playSequentially(avatar, cameraGallery, description, upload)
            start()
        }
    }

    private fun startGallery(){
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun startTakePhoto(){
        if(checkPermissions(CAMERA_PERMISSIONS)){
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.resolveActivity(requireActivity().packageManager)
            val file = createTempImageFile(requireActivity())
            val photoURI : Uri = FileProvider.getUriForFile(
                requireActivity(),
                "com.example.storyapp",
                file
            )

            currentPhotoPath = file.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }else{
            requestPermissionLauncher.launch(CAMERA_PERMISSIONS)
        }
    }

    private fun showLoading(isLoading : Boolean){
        viewBinding.pbLoading.visibility = if(isLoading) View.VISIBLE else View.GONE
    }

    companion object{
        private val CAMERA_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA
        )
        private val LOCATION_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

}