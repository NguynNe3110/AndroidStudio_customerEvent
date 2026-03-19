package com.uzuu.customer.core.di

import android.content.Context
import com.uzuu.customer.data.local.AppDatabase
import com.uzuu.customer.data.local.datasource.UserDataLocalSource
import com.uzuu.customer.data.remote.RetrofitProvider
import com.uzuu.customer.data.remote.datasource.AuthRemoteDataSource
import com.uzuu.customer.data.remote.datasource.CartRemoteDataSource
import com.uzuu.customer.data.remote.datasource.CategoryRemoteDataSource
import com.uzuu.customer.data.remote.datasource.EventRemoteDataSource
import com.uzuu.customer.data.repository.AuthRepositoryImpl
import com.uzuu.customer.data.repository.CartRepositoryImpl
import com.uzuu.customer.data.repository.CategoryRepositoryImpl
import com.uzuu.customer.data.repository.EventRepositoryImpl
import com.uzuu.customer.data.repository.UserRepositoryImpl

class AppContainer(context: Context) {
    private val db = AppDatabase.get(context)

    // ── APIs ─────────────────────────────────────────────────────────────────
    val authApi     = RetrofitProvider.authApi
    val eventApi    = RetrofitProvider.eventApi
    val categoryApi = RetrofitProvider.categoryApi
    val cartApi     = RetrofitProvider.cartApi          // ← THÊM

    // ── Local ─────────────────────────────────────────────────────────────────
    val userLocal = UserDataLocalSource(db.userDao())

    // ── Remote data sources ───────────────────────────────────────────────────
    val authRemote     = AuthRemoteDataSource(authApi     = authApi)
    val eventRemote    = EventRemoteDataSource(eventApi   = eventApi)
    val categoryRemote = CategoryRemoteDataSource(categoryApi = categoryApi)
    val cartRemote     = CartRemoteDataSource(cartApi     = cartApi) // ← THÊM

    // ── Repositories ──────────────────────────────────────────────────────────
    val userRepo     = UserRepositoryImpl(userLocal)
    val authRepo     = AuthRepositoryImpl(authRemote)
    val eventRepo    = EventRepositoryImpl(eventRemote)
    val categoryRepo = CategoryRepositoryImpl(categoryRemote)
    val cartRepo     = CartRepositoryImpl(cartRemote)  // ← THÊM
}