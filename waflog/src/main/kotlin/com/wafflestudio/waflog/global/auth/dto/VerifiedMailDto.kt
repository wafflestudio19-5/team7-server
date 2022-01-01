package com.wafflestudio.waflog.global.auth.dto

import com.wafflestudio.waflog.global.auth.model.VerificationToken

class VerifiedMailDto {
    data class Response(
        val email: String
    ) {
        constructor(verificationToken: VerificationToken) : this(
            email = verificationToken.email
        )
    }
}
