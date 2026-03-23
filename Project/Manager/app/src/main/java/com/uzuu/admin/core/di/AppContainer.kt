package com.uzuu.admin.core.di

import android.content.Context
import com.uzuu.admin.data.remote.RetrofitProvider
import com.uzuu.admin.data.remote.datasource.AuthRemoteDataSource
import com.uzuu.admin.data.remote.datasource.CheckInRemoteDataSource
import com.uzuu.admin.data.repository.AuthRepositoryImpl
import com.uzuu.admin.data.repository.CheckInRepositoryImpl
import com.uzuu.admin.data.session.SessionManager
import com.uzuu.admin.domain.repository.AuthRepository
import com.uzuu.admin.domain.repository.CheckInRepository

/**
 * Manual DI container — khởi tạo một lần trong MainActivity.
 * BUG FIX: nhận Context để init SessionManager trước khi dùng.
 */
class AppContainer(context: Context) {

    init {
        SessionManager.init(context)
    }

    val authRepo: AuthRepository by lazy {
        AuthRepositoryImpl(
            AuthRemoteDataSource(RetrofitProvider.authApi)
        )
    }

    val checkInRepo: CheckInRepository by lazy {
        CheckInRepositoryImpl(
            CheckInRemoteDataSource(RetrofitProvider.checkInApi)
        )
    }
}