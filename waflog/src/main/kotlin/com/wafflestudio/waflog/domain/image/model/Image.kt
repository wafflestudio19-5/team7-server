package com.wafflestudio.waflog.domain.image.model

import com.wafflestudio.waflog.domain.model.BaseTimeEntity
import com.wafflestudio.waflog.domain.post.model.Post
import com.wafflestudio.waflog.domain.user.model.User
import javax.validation.constraints.NotNull
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "token"])])
class Image(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @field:NotNull
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    val post: Post?,

    @field:NotBlank
    val token: String,

    val originalName: String

) : BaseTimeEntity()
