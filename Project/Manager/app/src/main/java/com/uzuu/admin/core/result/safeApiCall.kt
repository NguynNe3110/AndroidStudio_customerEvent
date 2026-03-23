package com.uzuu.admin.core.result

import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(block: suspend () -> T): ApiResult<T> {
    return try {
        ApiResult.Success(block())
    } catch (e: IOException) {
        ApiResult.Error("Lỗi mạng — kiểm tra kết nối", e)
    } catch (e: HttpException) {
        val errorBody = e.response()?.errorBody()?.string()
        ApiResult.Error(errorBody ?: "HTTP ${e.code()} ${e.message()}", e)
    } catch (e: Exception) {
        ApiResult.Error("Lỗi không xác định: ${e.message}", e)
    }
}