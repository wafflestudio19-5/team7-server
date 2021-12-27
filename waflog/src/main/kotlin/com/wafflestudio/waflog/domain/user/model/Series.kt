package com.wafflestudio.waflog.domain.user.model

import com.wafflestudio.waflog.domain.model.BaseEntity
import com.wafflestudio.waflog.domain.post.model.Post
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
class Series (
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @field:NotNull
    val user: User,

    @field:NotBlank
    val name: String,

    @OneToMany(mappedBy = "series")
    var posts: MutableList<Post>
        ): BaseEntity()
