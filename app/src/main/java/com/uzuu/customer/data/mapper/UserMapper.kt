package com.uzuu.customer.data.mapper

import com.uzuu.customer.data.local.entity.UsersEntity
import com.uzuu.customer.domain.model.Users


fun Users.userdomainToDto(): UsersEntity{
    return UsersEntity(
        id = id,
        username = username,
        password = password,
        email = email,
        fullName = fullName,
        phoneNumber = phoneNumber,
        address = address
    )
}