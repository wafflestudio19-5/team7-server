package com.wafflestudio.toy.domain.user.model

import com.wafflestudio.toy.domain.model.BaseTimeEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
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

    val name: String? = "",

    val image: String? = "", // image file url

    @Column(name = "page_title")
    @field:NotBlank
    val pageTitle: String? = "0",

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
) : BaseTimeEntity()
