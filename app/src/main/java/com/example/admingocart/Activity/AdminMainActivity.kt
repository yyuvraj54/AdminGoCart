package com.example.admingocart.Activity

import android.app.Activity
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.admingocart.R
import com.example.admingocart.databinding.ActivityMainBinding
import com.example.admingocart.databinding.ActivityUsersMainBinding

class AdminMainActivity : AppCompatActivity() {

    private  lateinit var  binding : ActivityUsersMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUsersMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        NavigationUI.setupWithNavController(binding.bottomMenu , Navigation.findNavController(this,R.id.fragmentContainerView2))


    }
}