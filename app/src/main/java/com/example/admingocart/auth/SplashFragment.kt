package com.example.admingocart.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.admingocart.Activity.AdminMainActivity
import com.example.admingocart.R
import com.example.admingocart.databinding.FragmentSplashBinding
import com.example.admingocart.viewModel.AuthViewModel
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {
    private val viewModel: AuthViewModel by viewModels()
    private lateinit var binding : FragmentSplashBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =FragmentSplashBinding.inflate(layoutInflater)

        Handler(Looper.getMainLooper()).postDelayed({
            lifecycleScope.launch {
                viewModel.isCurrentUser.collect{
                    if(it){
                        startActivity(Intent(requireActivity() , AdminMainActivity::class.java))
                        requireActivity().finish()
                    }
                    else{
                        findNavController().navigate(R.id.action_splashFragment_to_signinFragment)
                    }
                }
            }


        },3000)


        return binding.root
    }
}