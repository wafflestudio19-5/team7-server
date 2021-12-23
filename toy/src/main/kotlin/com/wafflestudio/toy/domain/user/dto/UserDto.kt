package com.wafflestudio.toy.domain.user.dto

import com.wafflestudio.toy.domain.user.model.User

class UserDto {
    data class SimpleResponse(
        val username: String,
        val image: String
    ) {
        constructor(user: User) : this(
            username = user.username,
            image = user.image
        )
    }
}