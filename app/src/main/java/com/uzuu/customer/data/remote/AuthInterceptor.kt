package com.uzuu.customer.data.remote

import com.uzuu.customer.data.session.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val token = SessionManager.getToken()

        // THÊM DÒNG NÀY
        println("DEBUG [AuthInterceptor] token = $token | url = ${request.url}")

        val newRequest = if (token != null) {
            request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            request
        }
        return chain.proceed(newRequest)
    }
}