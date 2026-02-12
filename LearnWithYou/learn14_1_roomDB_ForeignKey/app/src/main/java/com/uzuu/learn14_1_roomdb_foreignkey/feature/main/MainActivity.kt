package com.uzuu.learn14_1_roomdb_foreignkey.feature.main

import android.app.ProgressDialog.show
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.room.Room
import com.uzuu.learn14_1_roomdb_foreignkey.data.local.AppDatabase
import com.uzuu.learn14_1_roomdb_foreignkey.data.local.dao.ClassDao
import com.uzuu.learn14_1_roomdb_foreignkey.data.local.dao.UserDao
import com.uzuu.learn14_1_roomdb_foreignkey.data.repository.DbRepository
import com.uzuu.learn14_1_roomdb_foreignkey.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels {
        val db = AppDatabase.get(applicationContext)
        val repo = DbRepository(db.classDao(), db.userDao())
        MainViewModelFactory(repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // click actions
        binding.btnAddClass.setOnClickListener {
            viewModel.onAddClass(
                idText = binding.editClassId.text.toString(),
                name = binding.editClassName.text.toString()
            )
        }

        binding.btnUpdClass.setOnClickListener {
            viewModel.onUpdateClass(
                idText = binding.editClassId.text.toString(),
                name = binding.editClassName.text.toString()
            )
        }

        binding.btnDelClass.setOnClickListener {
            viewModel.onDeleteClass(
                idText = binding.editClassId.text.toString()
            )
        }

        binding.btnAddStudent.setOnClickListener {
            viewModel.onAddStudent(
                idStudentText = binding.editStudentId.text.toString(),
                idClassText = binding.editClassIdForeig.text.toString(),
                name = binding.editStudentName.text.toString()
            )
        }

        binding.btnUpdStudent.setOnClickListener {
            viewModel.onUpdateStudent(
                idStudentText = binding.editStudentId.text.toString(),
                idClassText = binding.editClassIdForeig.text.toString(),
                name = binding.editStudentName.text.toString()
            )
        }

        binding.btnDelStudent.setOnClickListener {
            viewModel.onDeleteStudent(
                idStudentText = binding.editStudentId.text.toString()
            )
        }

        binding.btnDelAll.setOnClickListener {
            viewModel.onClearAll()
        }

        // collect state + event
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.uiState.collect { state ->
                        binding.textStatus.text = state.error
                        binding.textClass.text = state.textClass
                        binding.textInfStudent.text = state.textInfUser
                        binding.textInfClass.text = state.textInfClass
                    }
                }

                launch {
                    viewModel.uiEvent.collect { e ->
                        when (e) {
                            is MainUiEvent.Toast ->
                                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}
