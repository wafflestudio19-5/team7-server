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

    val image: String =
        "https://wafflestudio.com/_next/image?url=%2Fimages%2Ficon_intro.svg&w=640&q=75",

    var jwt: String
) : BaseEntity()
