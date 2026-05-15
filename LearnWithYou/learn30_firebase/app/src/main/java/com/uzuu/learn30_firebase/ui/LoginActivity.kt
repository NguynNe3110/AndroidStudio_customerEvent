package com.uzuu.learn30_firebase.ui

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.uzuu.learn30_firebase.R
import com.uzuu.learn30_firebase.ui.MainActivity
import com.uzuu.learn30_firebase.ui.RegisterActivity
import com.uzuu.learn30_firebase.viewmodel.AuthViewModel

class LoginActivity : AppCompatActivity() {

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val edtEmail = findViewById<EditText>(R.id.edtEmail)
        val edtPassword = findViewById<EditText>(R.id.edtPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val txtRegister = findViewById<TextView>(R.id.txtRegister)

        // check login
        viewModel.checkLogin()

        viewModel.user.observe(this) {
            if (it != null) {
                goToMain()
            }
        }

        viewModel.error.observe(this) {
            it?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        btnLogin.setOnClickListener {
            viewModel.login(
                edtEmail.text.toString(),
                edtPassword.text.toString()
            )
        }

        txtRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()

        val user = viewModel.user.value
        if (user != null) {
            goToMain()
        }
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}