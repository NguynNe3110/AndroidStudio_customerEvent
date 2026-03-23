package com.uzuu.admin.data.remote.datasource

import com.uzuu.admin.data.remote.api.CheckInApi
import com.uzuu.admin.data.remote.dto.BaseResponseDto
import com.uzuu.admin.data.remote.dto.response.CheckInResponseDto
import retrofit2.http.Query

class CheckInRemoteDataSource(
    private val api: CheckInApi
) {
    suspend fun checkIn( ticketCode: String ): BaseResponseDto<CheckInResponseDto> {
        return api.checkIn(ticketCode)
    }
}