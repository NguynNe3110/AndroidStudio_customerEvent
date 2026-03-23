package com.uzuu.admin.domain.repository

import com.uzuu.admin.core.result.ApiResult
import com.uzuu.admin.domain.model.CheckInResult
import com.uzuu.admin.domain.model.Login

interface AuthRepository {
    suspend fun login(request: Login): ApiResult<String>  // returns token
}

interface CheckInRepository {
    suspend fun checkIn(ticketCode: String): ApiResult<CheckInResult>
}