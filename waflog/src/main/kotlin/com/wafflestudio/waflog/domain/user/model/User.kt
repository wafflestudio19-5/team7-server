package com.wafflestudio.waflog.domain.user.model

import com.fasterxml.jackson.annotation.JsonIgnore
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

    var userId: String? = "",

    @Column(unique = true)
    var username: String? = "",

    var intro: String? = "",

    val shortIntro: String? = "",

    val longIntro: String? = "",

    val image: String? = "default image url", // image file url

    @Column(name = "page_title")
    @field:NotBlank
    val pageTitle: String? = "default",

    @Column(name = "public_email")
    @field:Email
    val publicEmail: String? = "",

    @Column(name = "github_id")
    val githubId: String? = "",

    @Column(name = "facebook_id")
    val facebookId: String? = "",

    @Column(name = "twitter_id")
    val twitterId: String? = "",

    val homepage: String? = "",

    @Column(name = "comment_noti")
    val commentNotification: Boolean? = false,

    @Column(name = "update_noti")
    val updateNotification: Boolean? = false,

    var enabled: Boolean? = false,

    @OneToMany(mappedBy = "user")
    @OrderBy("id")
    var posts: MutableList<Post> = mutableListOf()
) : BaseTimeEntity()
