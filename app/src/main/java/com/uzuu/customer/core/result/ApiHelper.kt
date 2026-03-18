package com.uzuu.customer.core.result

import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(block: suspend () -> T): ApiResult<T> {
    //    block là một biến
//    Kiểu của nó là: một hàm
//    Không nhận tham số
//    Trả về kiểu T
//    Là suspend function
    return try {
        ApiResult.Success(block())
    } catch (e: IOException) {
        ApiResult.Error("Network error (mất mạng/timeout)", e)
    } catch (e: HttpException) {
        val errorBody = e.response()?.errorBody()?.string()  // nhớ: chỉ đọc 1 lần
        ApiResult.Error(
            errorBody ?: "HTTP ${e.code()} ${e.message()}",
            e
        )
    } catch (e: Exception) {
        ApiResult.Error("Unknown error: ${e.message}", e)
    }
}