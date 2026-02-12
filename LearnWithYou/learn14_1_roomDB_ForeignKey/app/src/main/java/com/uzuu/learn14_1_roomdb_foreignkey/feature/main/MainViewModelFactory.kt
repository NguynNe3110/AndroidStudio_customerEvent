package com.uzuu.learn14_1_roomdb_foreignkey.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uzuu.learn14_1_roomdb_foreignkey.data.repository.DbRepository

class MainViewModelFactory(
    private val repo: DbRepository
): ViewModelProvider.Factory {
    override fun <T: ViewModel> create (modelClass: Class<T>) :T {
        return MainViewModel(repo) as T
    }
}