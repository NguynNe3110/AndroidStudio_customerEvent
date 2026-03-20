package com.uzuu.customer.feature.middle.personal

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.uzuu.customer.R
import com.uzuu.customer.data.session.SessionManager
import com.uzuu.customer.databinding.FragmentPersonalBinding
import com.uzuu.customer.feature.MainActivity
import kotlinx.coroutines.launch

class PersonalFragment : Fragment() {

    private var _binding: FragmentPersonalBinding? = null
    val binding get() = _binding!!

    // ── Chọn ảnh từ gallery ───────────────────────────────────────────────────
    private val pickImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            // Giữ quyền đọc URI vĩnh viễn (tránh mất quyền sau restart)
            try {
                requireContext().contentResolver.takePersistableUriPermission(
                    it, Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: Exception) { }
            viewModel.saveAvatarUri(it.toString())
        }
    }

    private val viewModel: PersonalViewModel by viewModels {
        PersonalFactory((requireActivity() as MainActivity).container.userRepo)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.init()
        setupClickListeners()
        observeState()
        observeEvent()
    }

    private fun setupClickListeners() {
        // Click vào avatar → mở gallery
        binding.imgAvatar.setOnClickListener {
            pickImage.launch("image/*")
        }

        // Row chỉnh sửa thông tin
        binding.rowEditInfo.setOnClickListener {
            viewModel.onEditInfo()
        }

        // Đăng xuất
        binding.btnLogout.setOnClickListener {
            SessionManager.clear()
            val rootNavController = (requireActivity() as MainActivity)
                .supportFragmentManager
                .findFragmentById(R.id.root_nav_host)
                .let { it as NavHostFragment }
                .navController
            rootNavController.navigate(
                R.id.auth_graph,
                null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.root_graph, true)
                    .build()
            )
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->

                    // Avatar
                    if (!state.avatarUri.isNullOrBlank()) {
                        try {
                            binding.imgAvatar.setImageURI(Uri.parse(state.avatarUri))
                        } catch (_: Exception) { }
                    }

                    // Tên hiển thị — ưu tiên fullName, fallback về username
                    val displayName = state.fullName.ifBlank { state.username }
                    binding.txtDisplayName.text = "Xin chào: $displayName"

                    // Loading overlay (tuỳ chọn)
                    binding.progress.visibility = if (state.isLoading) View.VISIBLE else View.GONE
                }
            }
        }
    }

    private fun observeEvent() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect { event ->
                    when (event) {
                        is PersonalUiEvent.Toast ->
                            Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()

                        is PersonalUiEvent.NavigateToEditInfo ->
                            findNavController().navigate(R.id.action_personal_to_editInfo)

                        is PersonalUiEvent.NavigateToLogin -> { /* handled by logout button */ }
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