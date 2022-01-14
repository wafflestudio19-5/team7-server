package com.wafflestudio.waflog.domain.post.model

import com.wafflestudio.waflog.domain.model.BaseEntity
import javax.persistence.*

@Entity
class PostToken(

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", referencedColumnName = "id", unique = true)
    val post: Post,

    var token: String
) : BaseEntity()
