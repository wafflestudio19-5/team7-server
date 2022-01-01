package com.wafflestudio.waflog.global.auth.model

import com.wafflestudio.waflog.domain.model.BaseTimeEntity
import com.wafflestudio.waflog.domain.user.model.User
import javax.persistence.*

@Entity
@Table(name = "token_user")
class VerificationTokenUser(
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    val user: User,

    val token: String

) : BaseTimeEntity()
