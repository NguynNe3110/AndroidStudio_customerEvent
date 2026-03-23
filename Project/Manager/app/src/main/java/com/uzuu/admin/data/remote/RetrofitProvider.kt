package com.uzuu.admin.data.remote

import com.uzuu.admin.data.remote.api.AuthApi
import com.uzuu.admin.data.remote.api.CheckInApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitProvider {

    private const val BASE_URL = "http://192.168.1.6:8082/event-mng/"

    private val client: OkHttpClient by lazy {
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .addInterceptor(logger)
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authApi: AuthApi       by lazy { retrofit.create(AuthApi::class.java) }
    val checkInApi: CheckInApi by lazy { retrofit.create(CheckInApi::class.java) }
}