package com.wafflestudio.toy.domain.user.dto

import com.wafflestudio.toy.domain.user.model.User

class UserDto {
    data class SimpleResponse(
        val email: String
    ) {
        constructor(user: User) : this(
            email = user.email
        )
    }
    data class SignUpEmailRequest(
        val email: String
    )

    data class SignUpRequest(
        val token: String,
        val username: String,
        val userid: String,
        val intro: String
    )
}
