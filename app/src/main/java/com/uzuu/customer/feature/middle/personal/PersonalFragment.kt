package com.uzuu.customer.feature.middle.personal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.uzuu.customer.R
import com.uzuu.customer.data.session.SessionManager
import com.uzuu.customer.databinding.FragmentPersonalBinding
import com.uzuu.customer.feature.MainActivity

class PersonalFragment : Fragment() {
    private var _binding: FragmentPersonalBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnLogout.setOnClickListener {
            SessionManager.clear()

            // Lấy root NavController thay vì local
            val rootNavController = (requireActivity() as MainActivity)
                .supportFragmentManager
                .findFragmentById(R.id.root_nav_host)
                .let { it as androidx.navigation.fragment.NavHostFragment }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
