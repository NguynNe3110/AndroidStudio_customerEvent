package com.uzuu.customer.feature.middle.personal

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
import com.uzuu.customer.databinding.FragmentPersonalInfoBinding
import com.uzuu.customer.feature.MainActivity
import kotlinx.coroutines.launch

class PersonalInfoFragment : Fragment() {

    private var _binding: FragmentPersonalInfoBinding? = null
    val binding get() = _binding!!

    // Dùng chung ViewModel với PersonalFragment thông qua cùng Factory
    // (Không dùng activityViewModels vì scope khác NavGraph)
    private val viewModel: PersonalViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    ) {
        PersonalFactory((requireActivity() as MainActivity).container.userRepo)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        prefillFields()
        setupSaveButton()
        observeState()
        observeEvent()
    }

    // Điền sẵn thông tin hiện tại vào các ô
    private fun prefillFields() {
        val state = viewModel.state.value
        binding.edtEmail.setText(state.email)
        binding.edtFullName.setText(state.fullName)
        binding.edtPhoneNumber.setText(state.phone)
        binding.edtAddress.setText(state.address)
        // username: read-only, chỉ hiển thị
        binding.edtUsername.setText(state.username)
        binding.edtUsername.isEnabled = false
    }

    private fun setupSaveButton() {
        binding.btnRegister.setOnClickListener {
            val password = binding.edtPassword.text.toString().trim()
            val email    = binding.edtEmail.text.toString().trim()
            val fullName = binding.edtFullName.text.toString().trim()
            val phone    = binding.edtPhoneNumber.text.toString().trim()
            val address  = binding.edtAddress.text.toString().trim()

            if (email.isBlank() || fullName.isBlank() || phone.isBlank() || address.isBlank()) {
                Toast.makeText(context, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.updateInfo(password, email, fullName, phone, address)
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    // Disable form khi đang loading
                    val editable = !state.isLoading
                    binding.edtEmail.isEnabled       = editable
                    binding.edtFullName.isEnabled    = editable
                    binding.edtPhoneNumber.isEnabled = editable
                    binding.edtAddress.isEnabled     = editable
                    binding.edtPassword.isEnabled    = editable
                    binding.btnRegister.isEnabled    = editable
                    binding.progress.visibility      = if (state.isLoading) View.VISIBLE else View.GONE
                }
            }
        }
    }

    private fun observeEvent() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect { event ->
                    when (event) {
                        is PersonalUiEvent.Toast -> {
                            Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                            // Nếu thành công → quay lại Personal
                            if (event.message.contains("thành công")) {
                                findNavController().popBackStack()
                            }
                        }
                        else -> {}
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