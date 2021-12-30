package com.wafflestudio.toy.domain.tag.model

import com.wafflestudio.toy.domain.model.BaseEntity
import com.wafflestudio.toy.domain.post.model.Post
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "post_tag")
class PostTag(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    @field:NotNull
    val post: Post,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", referencedColumnName = "id")
    @field:NotNull
    val tag: Tag,

) : BaseEntity()
