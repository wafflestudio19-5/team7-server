package com.wafflestudio.waflog.global.auth.model

import com.wafflestudio.waflog.domain.model.BaseTimeEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

@Entity
@Table(name="token_user")
class VerificationTokenUser(
    @field:Email
    val email: String,

    val token: String

): BaseTimeEntity()
