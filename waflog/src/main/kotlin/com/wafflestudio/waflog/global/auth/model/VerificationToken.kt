package com.wafflestudio.waflog.global.auth.model

import com.wafflestudio.waflog.domain.model.BaseTimeEntity
import javax.persistence.Entity
import javax.persistence.Table
import javax.validation.constraints.Email

@Entity
@Table(name = "verify_token")
class VerificationToken(
    @field:Email
    val email: String,

    val token: String,

    val role: String = "user"

) : BaseTimeEntity()
