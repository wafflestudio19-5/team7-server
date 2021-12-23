package com.wafflestudio.toy.domain.post.model

import com.wafflestudio.toy.domain.model.BaseTimeEntity
import com.wafflestudio.toy.domain.user.model.Series
import com.wafflestudio.toy.domain.user.model.User
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
class Post (

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @field:NotNull
    val user: User,

    @field:NotBlank
    val title: String,

    @field:NotBlank
    val content: String,

    @field:Min(0)
    val likes: Int,

    val thumbnail: String, // thumbnail image file url

    val summary: String,

    val private: Boolean,

    @field:NotBlank
    val url: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id", referencedColumnName = "id")
    val series: Series?,

    ): BaseTimeEntity()
