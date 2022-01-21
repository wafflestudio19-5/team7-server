package com.wafflestudio.waflog.domain.user.model

import com.wafflestudio.waflog.domain.image.model.Image
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

    var shortIntro: String = "",

    val longIntro: String = "",

    var image: String =
        "https://wafflestudio.com/_next/image?url=%2Fimages%2Ficon_intro.svg&w=640&q=75", // image file url

    @Column(name = "page_title")
    @field:NotBlank
    var pageTitle: String = "",

    @Column(name = "public_email")
    @field:Email
    var publicEmail: String = "",

    @Column(name = "github_id")
    var githubId: String = "",

    @Column(name = "facebook_id")
    var facebookId: String = "",

    @Column(name = "twitter_id")
    var twitterId: String = "",

    var homepage: String = "",

    @Column(name = "comment_noti")
    val commentNotification: Boolean = false,

    @Column(name = "update_noti")
    val updateNotification: Boolean = false,

    @OneToMany(mappedBy = "user")
    var images: MutableList<Image> = mutableListOf(),

    @OneToMany(mappedBy = "user")
    @OrderBy("id DESC")
    var posts: MutableList<Post> = mutableListOf(),

    @OneToMany(mappedBy = "user")
    @OrderBy("createdAt DESC")
    var likedPosts: MutableList<Likes> = mutableListOf(),

    @OneToMany(mappedBy = "user")
    @OrderBy("updatedAt DESC")
    var readPosts: MutableList<Reads> = mutableListOf(),

    @OneToMany(mappedBy = "user")
    var series: MutableList<Series> = mutableListOf()

) : BaseTimeEntity()
