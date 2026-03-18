package com.uzuu.customer.feature.start.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.uzuu.customer.R
import com.uzuu.customer.databinding.FragmentLoginBinding
import com.uzuu.customer.feature.MainActivity
import com.uzuu.customer.feature.start.login.LoginFragmentArgs
import com.uzuu.customer.feature.start.login.LoginFragmentDirections
import kotlinx.coroutines.launch
import kotlin.getValue

class LoginFragment: Fragment() {
    private var _bindind : FragmentLoginBinding?= null

    val binding get() = _bindind!!

    private val args : LoginFragmentArgs by navArgs()
    private val viewModel : LoginViewModel by viewModels {
        val repo = (requireActivity() as MainActivity).container.authRepo
        val userRepo = (requireActivity() as MainActivity).container.userRepo
        LoginFactory(repo, userRepo)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bindind = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupEvent()

        // có thể làm cho quên mk từ login -> forget
        val username = args.usernameStr
        if(!username.isNullOrEmpty()) {
            viewModel.observe(username)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginState.collect { state ->
                    binding.edtUsername.setText(state.username)

                    binding.progress.visibility = if(state.isLoading) View.VISIBLE else View.GONE

                    val editable = !state.isLoading
                    binding.edtUsername.isEnabled = editable
                    binding.edtPassword.isEnabled = editable
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginEvent.collect { event ->
                    when(event) {
                        is LoginUiEvent.Toast -> {
                            Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                        }
                        is LoginUiEvent.navigateRegister -> {
                            val act = LoginFragmentDirections.actionLoginToRegister()
                            findNavController().navigate(act)
                        }

                        is LoginUiEvent.navigateForget -> {
                            val act = LoginFragmentDirections.actionLoginToForget()
                            findNavController().navigate(act)
                        }

                        is LoginUiEvent.navigateHome -> {
                            findNavController().navigate(
                                R.id.mainFragment,
                                null,
                                NavOptions.Builder()
                                    .setPopUpTo(R.id.auth_graph, true)
                                    .build()
                            )
                        }
                    }
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _bindind = null
    }

    private fun setupEvent() {
        binding.txtForgetPassword.setOnClickListener {
            viewModel.onClickForgetPass(
                binding.edtUsername.toString().trim()
            )
        }

        binding.txtRegisterNow.setOnClickListener {
            viewModel.onClickRegister()
        }

        binding.btnLoginNow.setOnClickListener {
            viewModel.onClickLogin(
                binding.edtUsername.text.toString().trim(),
                binding.edtPassword.text.toString().trim()
            )
        }
    }
}