package com.wafflestudio.waflog.domain.post.model

import com.wafflestudio.waflog.domain.model.BaseTimeEntity
import com.wafflestudio.waflog.domain.user.model.User
import javax.persistence.*
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

@Entity
class Comment(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    val user: User?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    val post: Post,

    @Column(name = "root_comment")
    var rootComment: Long = 0,
    var lft: Long = 1,
    var rgt: Long = 2,

    @field:Min(0)
    val depth: Int = 0,

    @field:NotBlank
    val content: String,

) : BaseTimeEntity()
