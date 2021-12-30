package com.wafflestudio.toy.global.auth.model

import com.wafflestudio.toy.domain.model.BaseTimeEntity
import com.wafflestudio.toy.domain.user.model.User
import javax.persistence.*

@Entity
@Table(name = "verify_token")
class VerificationToken(
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    val user: User,

    val token: String

) : BaseTimeEntity()
