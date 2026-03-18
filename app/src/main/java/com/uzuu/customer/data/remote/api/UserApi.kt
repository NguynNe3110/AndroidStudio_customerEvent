package com.uzuu.customer.data.remote.api

import com.uzuu.customer.data.remote.dto.BaseResponseDto
import com.uzuu.customer.data.remote.dto.request.UserRequestDto
import com.uzuu.customer.data.remote.dto.response.UserResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApi {

    @GET("/users/{username}")
    suspend fun getInfoByUsername(
        @Path("username") username: String
    ): BaseResponseDto<UserResponseDto>

    @PUT("/users/{username}")
    suspend fun updateInfo(
        @Path("username") username: String,
        @Body request: UserRequestDto
    ):  BaseResponseDto<UserResponseDto>

    // có api get thong tin tai khoan hien tai
}