package com.example.storyapp.view.fragment

import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.storyapp.R
import com.example.storyapp.databinding.FragmentAddStoryBinding
import com.example.storyapp.repository.NetworkResult
import com.example.storyapp.util.createTempImageFile
import com.example.storyapp.util.reduceFileImage
import com.example.storyapp.util.uriToFile
import com.example.storyapp.viewmodel.StoryViewModel
import com.example.storyapp.viewmodel.ViewModelFactory
import java.io.File

class AddStoryFragment : Fragment() {

    private lateinit var viewBinding : FragmentAddStoryBinding
    private var getFile : File? = null
    private lateinit var currentPhotoPath : String
    private val sharedViewModel : StoryViewModel by activityViewModels{
        ViewModelFactory.getInstance(requireContext())
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

        viewBinding =  FragmentAddStoryBinding.inflate(inflater, container, false)

        viewBinding.btnUploadFromCamera.setOnClickListener {
            if(!allPermissionsGranted()){
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS
                )
            }else{
                startTakePhoto()
            }
        }

        viewBinding.btnUploadFromGallery.setOnClickListener {
            startGallery()
        }

        viewBinding.btnPostStory.setOnClickListener {
            uploadStory()
        }

        playAnimation()

        return viewBinding.root
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_CODE_PERMISSIONS){
            if(!allPermissionsGranted()){
                Toast.makeText(requireActivity(), resources.getString(R.string.camera_permission_denied), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all{
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun uploadStory(){
        val description = viewBinding.etDescription.text.toString()
        if(getFile != null && description.isNotEmpty()){
            val uploadedFile : File = reduceFileImage(getFile!!)
            sharedViewModel.postStory(description, uploadedFile).observe(viewLifecycleOwner){ networkResult ->
                when(networkResult){
                    is NetworkResult.Loading -> showLoading(true)
                    is NetworkResult.Error -> {
                        showLoading(false)
                        Toast.makeText(requireActivity(), networkResult.message, Toast.LENGTH_SHORT).show()
                    }
                    is NetworkResult.Success -> {
                        showLoading(false)
                        networkResult.data.apply {
                            if(!error){
                                Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
                                viewBinding.root.findNavController()
                                    .navigate(R.id.action_addStoryFragment_to_storiesFragment)
                            }
                        }
                    }
                }
            }
        }else{
            Toast.makeText(requireActivity(), "Silahkan isi deskripsi atau pilih gambar dahulu", Toast.LENGTH_SHORT).show()
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
    }

    private fun showLoading(isLoading : Boolean){
        viewBinding.pbLoading.visibility = if(isLoading) View.VISIBLE else View.GONE
    }

    companion object{
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

}