package com.example.storyapp.view.fragment

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.storyapp.R
import com.example.storyapp.databinding.FragmentRegisterBinding
import com.example.storyapp.viewmodel.AuthViewModel


class RegisterFragment : Fragment() {

    private lateinit var viewBinding : FragmentRegisterBinding
    private val sharedViewModel : AuthViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
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
            // Register process here, if succeeded navigate to login fragment
        }
        viewBinding.tvLogin.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

}