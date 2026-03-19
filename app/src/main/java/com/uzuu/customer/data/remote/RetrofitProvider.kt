package com.uzuu.customer.data.remote

import com.uzuu.customer.data.remote.api.AuthApi
import com.uzuu.customer.data.remote.api.CategoryApi
import com.uzuu.customer.data.remote.api.EventApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitProvider {
    private const val BASE_URL = "http://192.168.1.3:8080/event-mng/"

    //okhttp

    private val client: OkHttpClient by lazy {
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor()) // thêm dòng này
            .addInterceptor(logger)
            .build()
    }


    //http manager
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(com.uzuu.customer.data.remote.RetrofitProvider.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authApi: AuthApi by lazy {
        retrofit.create(AuthApi::class.java)
    }

    val eventApi: EventApi by lazy {
        retrofit.create(EventApi::class.java)
    }
    val categoryApi: CategoryApi by lazy {
        retrofit.create(CategoryApi::class.java) }
}