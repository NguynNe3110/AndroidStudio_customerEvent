package com.uzuu.learn16_hilt_dependencyinject

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.uzuu.learn16_hilt_dependencyinject.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MyViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        observeUi()
        setBtn()
    }

    fun setBtn() {
        binding.edtName.addTextChangedListener { name->
            viewModel.setTextFromInput(name.toString())
        }
    }

    fun observeUi(){
        lifecycleScope.launch {
            viewModel.uiState.collect {
                if(it.name.isEmpty()) binding.textHome.setText(viewModel.getText())
                else binding.textHome.setText(it.name)
            }
        }
    }
}