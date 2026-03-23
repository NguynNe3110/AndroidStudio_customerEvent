package com.uzuu.admin.data.remote.api

import com.uzuu.admin.data.remote.dto.BaseResponseDto
import com.uzuu.admin.data.remote.dto.response.CheckInResponseDto
import retrofit2.http.POST
import retrofit2.http.Query

interface CheckInApi {

    /**
     * POST /tickets/check-in?ticketCode=XXX
     * Quét mã vé để check-in (ban tổ chức / admin)
     */
    @POST("tickets/check-in")
    suspend fun checkIn(
        @Query("ticketCode") ticketCode: String
    ): BaseResponseDto<CheckInResponseDto>
}