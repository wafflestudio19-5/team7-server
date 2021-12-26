package com.wafflestudio.toy.domain.user.model

import com.wafflestudio.toy.domain.model.BaseTimeEntity
import com.wafflestudio.toy.domain.post.model.Post
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.OneToMany
import javax.persistence.Table
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "waffle_user")
class User (
    @Column(unique = true)
    @field:Email
    @field:NotBlank
    val email: String,

    val name: String,

    @Column(unique = true)
    @field:NotBlank
    val username: String,

    @field:NotBlank
    val password: String,

    val intro: String,

    val image: String, // image file url

    @Column(name = "page_title")
    @field:NotBlank
    val pageTitle: String,

    @Column(name = "public_email")
    @field:Email
    val publicEmail: String,

    @Column(name = "github_id")
    val githubId: String,

    @Column(name = "facebook_id")
    val facebookId: String,

    @Column(name = "twitter_id")
    val twitterId: String,

    val homepage: String,

    @Column(name = "comment_noti")
    val commentNotification: Boolean,

    @Column(name = "update_noti")
    val updateNotification: Boolean,

    @OneToMany(mappedBy = "user")
    var posts: MutableList<Post>
    ): BaseTimeEntity()
