package com.uzuu.customer.feature

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.uzuu.customer.R
import com.uzuu.customer.core.di.AppContainer
import com.uzuu.customer.data.session.SessionManager
import com.uzuu.customer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var container: AppContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        container = AppContainer(applicationContext)

        // Nếu đã có token → bỏ qua login, vào thẳng Home
        if (SessionManager.getToken() != null) {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.root_nav_host) as NavHostFragment
            navHostFragment.navController.navigate(
                R.id.mainFragment,
                null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.root_graph, true)  // xóa toàn bộ backstack
                    .build()
            )
        }
    }
}