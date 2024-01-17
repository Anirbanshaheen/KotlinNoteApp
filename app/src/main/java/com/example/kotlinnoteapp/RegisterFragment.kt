package com.example.kotlinnoteapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.kotlinnoteapp.databinding.FragmentRegisterBinding
import com.example.kotlinnoteapp.models.UserRequest
import com.example.kotlinnoteapp.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding : FragmentRegisterBinding
    private val authViewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        binding.btnSignUp.setOnClickListener {
            authViewModel.registerUser(UserRequest("abc@gmail.com", "12345678", "abc"))
        }

        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observers()
    }

    private fun observers() {

        lifecycleScope.launch {
            authViewModel.userResponseLiveData.collectLatest {
                binding.progressBar.isVisible = false
                when (it) {
                    is NetworkResult.SUCCESS -> {
                        findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
                    }
                    is NetworkResult.ERROR -> {
                        binding.txtError.text = it.message
                    }
                    is NetworkResult.LOADING -> {
                        binding.progressBar.isVisible = true
                    }
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}