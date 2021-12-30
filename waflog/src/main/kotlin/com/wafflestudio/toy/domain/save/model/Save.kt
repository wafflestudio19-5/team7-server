package com.wafflestudio.toy.domain.save.model

import com.wafflestudio.toy.domain.model.BaseTimeEntity
import com.wafflestudio.toy.domain.user.model.User
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
class Save(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @field:NotNull
    val user: User,

    @field:NotBlank
    val title: String,

    @field:NotBlank
    val content: String,

) : BaseTimeEntity()
