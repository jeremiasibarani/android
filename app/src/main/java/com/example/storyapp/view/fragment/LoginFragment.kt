package com.example.storyapp.view.fragment

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.storyapp.R
import com.example.storyapp.databinding.FragmentLoginBinding
import com.example.storyapp.repository.NetworkResult
import com.example.storyapp.util.isEmailValid
import com.example.storyapp.view.activity.StoryActivity
import com.example.storyapp.viewmodel.AuthViewModel
import com.example.storyapp.viewmodel.ViewModelFactory

class LoginFragment : Fragment() {

    private lateinit var viewBinding : FragmentLoginBinding
    private val sharedViewModel : AuthViewModel by activityViewModels{
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewBinding = FragmentLoginBinding.inflate(inflater, container, false)

        setupView()
        playAnimation()



        return viewBinding.root
    }

    private fun playAnimation(){
        val email = ObjectAnimator.ofFloat(viewBinding.etEmail, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(viewBinding.etPassword, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(viewBinding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val register = ObjectAnimator.ofFloat(viewBinding.linearLayoutCompat, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(email, password, login, register)
            start()
        }
    }

    private fun setupView(){

        viewBinding.tvRegister.setOnClickListener {
            viewBinding.root.findNavController()
                .navigate(R.id.registerFragment)
        }

        viewBinding.btnLogin.setOnClickListener {
            val email = viewBinding.etEmail.text.toString().trim()
            val password = viewBinding.etPassword.text.toString().trim()
            if(password.length < 8){
                viewBinding.etPassword.requestFocus()
            }else if(!isEmailValid(email)){
                viewBinding.etEmail.requestFocus()
            }else{
                sharedViewModel.login(email, password).observe(viewLifecycleOwner){networkResult ->
                    when(networkResult){
                        is NetworkResult.Loading -> showLoading(true)
                        is NetworkResult.Error -> {
                            showLoading(false)
                            Toast.makeText(requireActivity(), networkResult.message , Toast.LENGTH_SHORT).show()
                        }
                        is NetworkResult.Success -> {
                            showLoading(false)
                            if(!networkResult.data.error){
                                val intent = Intent(requireActivity(), StoryActivity::class.java)
                                requireActivity().apply {
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading : Boolean){
        viewBinding.pbLoading.visibility = if(isLoading) View.VISIBLE else View.GONE
    }


}