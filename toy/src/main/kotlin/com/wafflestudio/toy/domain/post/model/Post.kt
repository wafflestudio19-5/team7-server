package com.wafflestudio.toy.domain.post.model

import com.wafflestudio.toy.domain.model.BaseTimeEntity
import com.wafflestudio.toy.domain.tag.model.PostTag
import com.wafflestudio.toy.domain.user.model.Series
import com.wafflestudio.toy.domain.user.model.User
import org.hibernate.annotations.Formula
import javax.persistence.*
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
class Post(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @field:NotNull
    val user: User,

    @field:NotBlank
    val title: String,

    @field:NotBlank
    val content: String,

    @field:Min(0)
    val views: Int = 0,

    @field:Min(0)
    val likes: Int = 0,

    val thumbnail: String, // thumbnail image file url

    val summary: String,

    val private: Boolean,

    @Formula(value = "10 * views + 7 * likes + 3 * (SELECT count(1) FROM comment c WHERE c.post_id = id)")
    val trending: Int = 0,

    @field:NotBlank
    val url: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id", referencedColumnName = "id")
    val series: Series?,

    @OneToMany(mappedBy = "post")
    var comments: MutableList<Comment>,

    @OneToMany(mappedBy = "post")
    var postTags: MutableList<PostTag>
) : BaseTimeEntity() {

    fun getPrevPost(): Post? {
        val userPosts:List<Post> = this.user.posts
        var prevPost:Post = this
        var prevPostId:Long = -1L

        for (post in userPosts) {
            if(prevPostId == -1L && post.id < this.id){
                prevPost = post
                prevPostId = post.id
            }
            if (prevPostId < post.id && post.id < this.id){
                prevPost = post
                prevPostId = post.id
            }
        }
        return if (prevPost.id == this.id){
             null
        }else{
            prevPost
        }
    }

    fun getNextPost(): Post? {
        val userPosts:List<Post> = this.user.posts
        var nextPost = this
        var nextPostId = -1L

        for (post in userPosts) {
            if(nextPostId == -1L && post.id > this.id){
                nextPost = post
                nextPostId = post.id
            }
            if (nextPostId > post.id && post.id > this.id){
                nextPost = post
                nextPostId = post.id
            }
        }
        return if (nextPost.id == this.id){
            null
        }else{
            nextPost
        }
    }
}
