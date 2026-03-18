package com.uzuu.customer.feature.start.register

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
import androidx.navigation.fragment.findNavController
import com.uzuu.customer.databinding.FragmentRegisterBinding
import com.uzuu.customer.feature.MainActivity
import com.uzuu.customer.feature.start.register.RegisterFragmentDirections
import kotlinx.coroutines.launch
import kotlin.getValue

class RegisterFragment: Fragment() {
    private var _bindind : FragmentRegisterBinding?= null

    val binding get() = _bindind!!

    private val viewModel: RegisterViewModel by viewModels {
        val authRepo = (requireActivity() as MainActivity).container.authRepo
        val userRepo = (requireActivity() as MainActivity).container.userRepo
        RegisterFactory(authRepo, userRepo)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bindind = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupEvent()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.registerState.collect { state ->
                    val editable = !state.isLoading
                    binding.edtUsername.isEnabled = editable
                    binding.edtPassword.isEnabled = editable
                    binding.edtEmail.isEnabled = editable
                    binding.edtFullName.isEnabled = editable
                    binding.edtPhoneNumber.isEnabled = editable
                    binding.edtAddress.isEnabled = editable
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.registerEvent.collect { event ->
                    when(event) {
                        is RegisterUiEvent.Toast -> {
                            Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                        }
                        is RegisterUiEvent.navigateToLogin -> {
                            val act = RegisterFragmentDirections.actionRegisterToLogin(event.user)
                            findNavController().navigate(act)
                        }

                        is RegisterUiEvent.navigateWith -> {

                        }

                        is RegisterUiEvent.Loading -> {
                            binding.btnRegister.isEnabled = false
                            binding.progress.visibility = View.VISIBLE
                            binding.btnRegister.text = ""
                        }

                        is RegisterUiEvent.Success -> {
                            binding.btnRegister.isEnabled = true
                            binding.progress.visibility = View.GONE
                            binding.btnRegister.text = "Đăng nhập"
                        }

                        is RegisterUiEvent.Error -> {
                            binding.btnRegister.isEnabled = true
                            binding.progress.visibility = View.GONE
                            binding.btnRegister.text = "Đăng nhập"
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
        binding.btnRegister.setOnClickListener {
            viewModel.onClickRegister(
                binding.edtUsername.text.toString().trim(),
                binding.edtPassword.text.toString().trim(),
                binding.edtEmail.text.toString().trim(),
                binding.edtFullName.text.toString().trim(),
                binding.edtPhoneNumber.text.toString().trim(),
                binding.edtAddress.text.toString().trim()
            )
        }
    }
}