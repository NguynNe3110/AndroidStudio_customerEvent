package com.uzuu.admin.feature.login

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
import com.uzuu.admin.R
import com.uzuu.admin.databinding.FragmentLoginBinding
import com.uzuu.admin.feature.MainActivity
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels {
        LoginFactory((requireActivity() as MainActivity).container.authRepo)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupButtons()
        observeState()
        observeEvent()
    }

    private fun setupButtons() {
        binding.btnLogin.setOnClickListener {
            viewModel.onLogin(
                binding.edtUsername.text.toString().trim(),
                binding.edtPassword.text.toString().trim()
            )
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    val isEnabled = !state.isLoading
                    binding.edtUsername.isEnabled = isEnabled
                    binding.edtPassword.isEnabled = isEnabled
                    binding.btnLogin.isEnabled    = isEnabled
                    binding.progress.visibility   =
                        if (state.isLoading) View.VISIBLE else View.GONE
                    binding.btnLogin.text =
                        if (state.isLoading) "" else "Đăng nhập"
                }
            }
        }
    }

    private fun observeEvent() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect { event ->
                    when (event) {
                        is LoginUiEvent.Toast ->
                            Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()

                        is LoginUiEvent.NavigateToScan ->
                            findNavController().navigate(
                                R.id.scanFragment,
                                null,
                                NavOptions.Builder()
                                    .setPopUpTo(R.id.loginFragment, true) // xóa login khỏi backstack
                                    .build()
                            )
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}