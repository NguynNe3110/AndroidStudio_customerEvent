package com.uzuu.customer.data.mapper

import com.uzuu.customer.data.remote.dto.request.LoginRequestDto
import com.uzuu.customer.data.remote.dto.request.RegisterRequestDto
import com.uzuu.customer.domain.model.Login
import com.uzuu.customer.domain.model.Register


fun Register.registerDomainToDto() : RegisterRequestDto {
    return RegisterRequestDto(
        username = username,
        password = password,
        email = email,
        fullName = fullName,
        phone = phone,
        address = address
    )
}

fun RegisterRequestDto.registerRequestDtoToDomain() : Register {
    return Register(
        username = username,
        password = password,
        email= email,
        fullName = fullName,
        phone = phone,
        address = address
    )
}

fun LoginRequestDto.LoginRequestDtoToDomain() : Login {
    return Login(
        username = username,
        password = password
    )
}

fun Login.loginDomainToDto() : LoginRequestDto {
    return LoginRequestDto(
        username = username,
        password = password
    )
}