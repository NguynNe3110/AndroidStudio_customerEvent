package com.uzuu.admin.feature.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uzuu.admin.domain.repository.CheckInRepository

class ScanFactory(
    private val checkInRepo: CheckInRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScanViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ScanViewModel(checkInRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}