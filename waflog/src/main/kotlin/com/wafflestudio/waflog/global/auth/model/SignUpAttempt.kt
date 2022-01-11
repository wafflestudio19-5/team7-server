package com.wafflestudio.waflog.global.auth.model

import com.wafflestudio.waflog.domain.model.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import javax.validation.constraints.Email

@Entity
@Table(name = "signup_attempt")
class SignUpAttempt(
    @Column(unique = true)
    @field:Email
    val email: String,

    var jwt: String
) : BaseEntity()
