package com.uzuu.customer.feature

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.uzuu.customer.core.di.AppContainer
import com.uzuu.customer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var container: AppContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        container = AppContainer(applicationContext)
    }
}