package com.uzuu.learn4_roomdb_retrofitapi_basic.feature.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.uzuu.learn4_roomdb_retrofitapi_basic.data.local.AppDatabase
import com.uzuu.learn4_roomdb_retrofitapi_basic.data.remote.ApiClient
import com.uzuu.learn4_roomdb_retrofitapi_basic.data.repository.UserRepositoryImpl
import com.uzuu.learn4_roomdb_retrofitapi_basic.databinding.ActivityMainBinding
import com.uzuu.learn4_roomdb_retrofitapi_basic.domain.repository.UserRepository
import com.uzuu.learn4_roomdb_retrofitapi_basic.ui.adapter.UserAdapter
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: UserViewModel

    private val adapter = UserAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // init dependencies
        val db = AppDatabase.get(context = this)
        val repo: UserRepository = UserRepositoryImpl(
            api = ApiClient.api,
            dao = db.userDao()
        )

        viewModel = UserViewModel(repo)

        binding.recycler.layoutManager = LinearLayoutManager(this)
        //setup recycle
        binding.recycler.adapter = adapter

        //btn
        binding.btnRefresh.setOnClickListener { viewModel.refresh() }

        binding.btnDeleteAll.setOnClickListener { viewModel.deleteAll() }

        // observe uiState
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state ->
                        binding.progress.visibility =
                            if (state.isLoading) View.VISIBLE else View.GONE

                        //
                        adapter.submit(state.users)

                        binding.tvStatus.text = when {
                            state.isLoading -> "Loading..."
                            state.error != null -> "Error: ${state.error}"
                            else -> "Loaded: ${state.users.size} users (from DB)"
                        }

                        state.error?.let { Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show() }
                    }
                }
            }
        }
    }
}