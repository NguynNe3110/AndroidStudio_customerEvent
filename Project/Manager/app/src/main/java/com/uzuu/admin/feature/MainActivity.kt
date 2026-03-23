package com.uzuu.admin.feature

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.uzuu.admin.R
import com.uzuu.admin.core.di.AppContainer
import com.uzuu.admin.data.session.SessionManager
import com.uzuu.admin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var container: AppContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ① AppContainer nhận context → tự gọi SessionManager.init() bên trong
        container = AppContainer(applicationContext)

        // ② Nếu đã có token → bỏ qua login, vào thẳng Scan
        if (SessionManager.getToken() != null) {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.root_nav_host) as NavHostFragment
            navHostFragment.navController.navigate(
                R.id.scanFragment,          // ② fix: mainFragment không tồn tại
                null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.root_graph, true)
                    .build()
            )
        }
    }
}