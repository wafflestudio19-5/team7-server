package com.wafflestudio.waflog.global.oauth2.model

import com.wafflestudio.waflog.domain.model.BaseTimeEntity
import javax.persistence.Entity
import javax.persistence.Table
import javax.validation.constraints.Email

@Entity
@Table(name = "oauth2_verify_token")
class OAuth2VerificationToken(
    @field:Email
    val email: String,

    val token: String

) : BaseTimeEntity()
