package com.uzuu.learn19_1_navigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.uzuu.learn19_1_navigation.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
