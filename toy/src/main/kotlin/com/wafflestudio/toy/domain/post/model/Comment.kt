package com.wafflestudio.toy.domain.post.model

import com.wafflestudio.toy.domain.model.BaseTimeEntity
import com.wafflestudio.toy.domain.user.model.User
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
class Comment (

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    val user: User?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    val post: Post,

    @Column(name = "root_comment")
    val rootComment: Long,
    val lft: Long,
    val rgt: Long,

    @field:Min(0)
    val depth: Int,

    @field:NotBlank
    val content: String,

    ): BaseTimeEntity()
