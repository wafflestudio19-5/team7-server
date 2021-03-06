package com.wafflestudio.waflog.domain.user.model

import com.wafflestudio.waflog.domain.model.BaseTimeEntity
import com.wafflestudio.waflog.domain.post.model.Post
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "name"])])
class Series(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @field:NotNull
    val user: User,

    @field:NotBlank
    var name: String,

    @OneToMany(mappedBy = "series")
    @OrderBy("seriesOrder")
    var posts: MutableList<Post>
) : BaseTimeEntity()
