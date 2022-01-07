package com.wafflestudio.waflog.global.auth.dto

import com.wafflestudio.waflog.domain.user.dto.UserDto
import com.wafflestudio.waflog.global.auth.model.VerificationTokenPrincipal

data class VerificationTokenPrincipalDto(
    val user: UserDto.SimpleResponse?,
    val token: String
) {
    constructor(verificationTokenPrincipal: VerificationTokenPrincipal, jwtToken: String) : this(
        user = verificationTokenPrincipal.user?.let { UserDto.SimpleResponse(it) },
        token = jwtToken
    )
}
