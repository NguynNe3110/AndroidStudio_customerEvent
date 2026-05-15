package com.uzuu.learn30_firebase.ui

import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.uzuu.learn30_firebase.R
import com.uzuu.learn30_firebase.viewmodel.AuthViewModel

class RegisterActivity : AppCompatActivity() {

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val edtEmail = findViewById<EditText>(R.id.edtEmail)
        val edtPassword = findViewById<EditText>(R.id.edtPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        viewModel.user.observe(this) {
            if (it != null) {
                Toast.makeText(this, "Register OK", Toast.LENGTH_SHORT).show()
                finish() // quay về Login
            }
        }

        viewModel.error.observe(this) {
            it?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        btnRegister.setOnClickListener {
            viewModel.register(
                edtEmail.text.toString(),
                edtPassword.text.toString()
            )
        }
    }
}