package com.wafflestudio.waflog.global.auth.dto

import com.wafflestudio.waflog.global.auth.model.VerificationTokenUser

class VerifiedMailDto {
    data class Response(
        val email: String
    ) {
        constructor(verificationTokenUser: VerificationTokenUser) : this(
            email = verificationTokenUser.email
        )
    }
}
