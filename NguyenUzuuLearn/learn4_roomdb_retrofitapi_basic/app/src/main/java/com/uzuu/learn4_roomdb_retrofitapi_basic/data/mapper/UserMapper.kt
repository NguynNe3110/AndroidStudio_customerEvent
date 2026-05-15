package com.uzuu.learn4_roomdb_retrofitapi_basic.data.mapper

import com.uzuu.learn4_roomdb_retrofitapi_basic.data.local.entity.UserEntity
import com.uzuu.learn4_roomdb_retrofitapi_basic.data.remote.UserDto
import com.uzuu.learn4_roomdb_retrofitapi_basic.domain.model.User

// nhận vào dl kiểu UserEntity ,tên hàm là toDomain(), trả về 1 User gán = luôn
fun UserEntity.toDomain() : User = User (
    id = id,
    displayName = name
)

//fun UserDto.toDomain(): User {
//    return User(
//        id = id,
//        displayName = name
//    )
//}

//viết đầy đủ
fun toEntity(dto: UserDto) : UserEntity {
    return UserEntity(
        id = dto.id,
        name = dto.name
    )
}

//fun UserDto.toEntity(): UserEntity = UserEntity(
//    id = id,
//    name = name
//)