package com.wafflestudio.waflog.domain.user.model

import com.wafflestudio.waflog.domain.model.BaseTimeEntity
import com.wafflestudio.waflog.domain.post.model.Post
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "waffle_user")
class User(
    @Column(unique = true)
    @field:Email
    @field:NotBlank
    val email: String,

    @Column(unique = true)
    var userId: String,

    var name: String,

    val shortIntro: String = "",

    val longIntro: String = "",

    val image: String = "https://wafflestudio.com/_next/image?url=%2Fimages%2Ficon_intro.svg&w=640&q=75", // image file url

    @Column(name = "page_title")
    @field:NotBlank
    val pageTitle: String = "",

    @Column(name = "public_email")
    @field:Email
    val publicEmail: String = "",

    @Column(name = "github_id")
    val githubId: String = "",

    @Column(name = "facebook_id")
    val facebookId: String = "",

    @Column(name = "twitter_id")
    val twitterId: String = "",

    val homepage: String = "",

    @Column(name = "comment_noti")
    val commentNotification: Boolean = false,

    @Column(name = "update_noti")
    val updateNotification: Boolean = false,

    @OneToMany(mappedBy = "user")
    @OrderBy("id")
    var posts: MutableList<Post> = mutableListOf(),

) : BaseTimeEntity()
