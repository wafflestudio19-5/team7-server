package com.wafflestudio.waflog.domain.user.model

import com.wafflestudio.waflog.domain.model.BaseTimeEntity
import com.wafflestudio.waflog.domain.post.model.Post
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Index
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Entity
@Table(name = "read_user_post", indexes = [Index(columnList = "user_id, post_id")])
class Reads(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @field:NotNull
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    @field:NotNull
    val readPost: Post,

    var lastRead: LocalDateTime
) : BaseTimeEntity()
