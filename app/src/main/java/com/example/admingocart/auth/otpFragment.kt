package com.example.admingocart.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.admingocart.Activity.AdminMainActivity
import com.example.admingocart.R
import com.example.admingocart.Utils
import com.example.admingocart.databinding.FragmentOtpBinding
import com.example.admingocart.viewModel.AuthViewModel
import kotlinx.coroutines.launch

class otpFragment : Fragment() {
    private  lateinit var binding: FragmentOtpBinding
    private lateinit var userNumber : String

    private val viewModel: AuthViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOtpBinding.inflate(layoutInflater)

        getUserNumber()
        sendOTP()
        customizingEnteringOTP()
        onLoginBtnClick()
        onBackButtonClicked()

        return binding.root
    }

    private fun onLoginBtnClick() {
        binding.OtploginBtn.setOnClickListener {
            Utils.showDialog(requireContext(), "Signing In...")
            val editText = arrayOf(
                binding.code1,
                binding.code2,
                binding.code3,
                binding.code4,
                binding.code5,
                binding.code6
            )
            val otp = editText.joinToString("") { it.text.toString() }

            if (otp.length < editText.size) {
                Utils.showToast(requireContext(), "Please Enter correct otp")
            } else {
                editText.forEach { it.text?.clear(); it.clearFocus() }
                verifyOtp(otp)
            }

        }

    }
    private fun verifyOtp(otp: String) {

        viewModel.signInWithPhoneAuthCredential(otp,userNumber )
        lifecycleScope.launch {
            viewModel.isSignedInSuccessfully.collect{
                if(it){
                    Utils.hideDialog()
                    Utils.showToast(requireContext(),"Logged In ....")
                    startActivity(Intent(requireActivity() , AdminMainActivity::class.java))
                    requireActivity().finish()
                }
            }
        }

    }

    private fun sendOTP() {
        Utils.showDialog(requireContext(), "Sending OTP...")
        viewModel.apply {
            sendOTP(userNumber,requireActivity())


            // As hamara collect ek suspend function h to coroutines use karna hoga
            lifecycleScope.launch {
                otpSent.collect{

                    if(it == true){
                        Utils.hideDialog()
                        Utils.showToast(requireContext() , "OTP SENT...")
                    }

                }
            }

        }




    }
    private fun onBackButtonClicked() {
        binding.tbOtpFrag.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_otpFragment_to_signinFragment)
        }
    }
    private fun getUserNumber() {
        var bundle = arguments
        userNumber = bundle?.getString("number").toString()
        binding.num.text= "+91 $userNumber"

    }
    private fun customizingEnteringOTP(){

        val editText = arrayOf(binding.code1,binding.code2,binding.code3,binding.code4,binding.code5,binding.code6)

        for(i in editText.indices){
            editText[i].addTextChangedListener ( object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if(s?.length == 1){
                        if(i<editText.size -1){
                            editText[i+1].requestFocus()
                        }
                    }else if(s?.length == 0){
                        if(i>0){
                            editText[i-1].requestFocus()
                        }

                    }



                }


            } )
        }


    }

}