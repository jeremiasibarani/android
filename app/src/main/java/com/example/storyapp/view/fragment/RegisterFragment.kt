package com.example.storyapp.view.fragment

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.storyapp.databinding.FragmentRegisterBinding
import com.example.storyapp.repository.NetworkResult
import com.example.storyapp.util.isEmailValid
import com.example.storyapp.viewmodel.AuthViewModel
import com.example.storyapp.viewmodel.ViewModelFactory


class RegisterFragment : Fragment() {

    private lateinit var viewBinding : FragmentRegisterBinding
    private val sharedViewModel : AuthViewModel by activityViewModels{
        ViewModelFactory.getInstance(requireContext())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding =  FragmentRegisterBinding.inflate(inflater, container, false)

        setupView()
        playAnimation()

        return viewBinding.root
    }

    private fun playAnimation(){
        val name = ObjectAnimator.ofFloat(viewBinding.etName, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(viewBinding.etEmail, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(viewBinding.etPassword, View.ALPHA, 1f).setDuration(500)
        val register = ObjectAnimator.ofFloat(viewBinding.btnRegister, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(viewBinding.linearLayoutCompat, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(name, email, password, register, login)
            start()
        }
    }

    private fun setupView(){
        viewBinding.btnRegister.setOnClickListener {
            val name = viewBinding.etName.text.toString().trim()
            val email = viewBinding.etEmail.text.toString().trim()
            val password = viewBinding.etPassword.text.toString().trim()

            if(name.isEmpty()){
                viewBinding.etName.requestFocus()
            }else if(!isEmailValid(email)){
                viewBinding.etEmail.requestFocus()
            }else if(password.length < 8){
                viewBinding.etPassword.requestFocus()
            }else{
                sharedViewModel.register(name, email, password).observe(viewLifecycleOwner){networkResult ->
                    when(networkResult){
                        is NetworkResult.Loading -> showLoading(true)
                        is NetworkResult.Error -> {
                            showLoading(false)
                            Toast.makeText(requireActivity(), networkResult.message, Toast.LENGTH_SHORT).show()
                        }
                        is NetworkResult.Success -> {
                            showLoading(false)
                            if(!networkResult.data.error){
                                requireActivity().onBackPressed()
                            }
                        }
                    }
                }
            }

        }
        viewBinding.tvLogin.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun showLoading(isLoading : Boolean){
        viewBinding.pbLoading.visibility = if(isLoading) View.VISIBLE else View.GONE
    }

}