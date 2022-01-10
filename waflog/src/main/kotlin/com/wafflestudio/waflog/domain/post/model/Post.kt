package com.wafflestudio.waflog.domain.post.model

import com.wafflestudio.waflog.domain.model.BaseTimeEntity
import com.wafflestudio.waflog.domain.tag.model.PostTag
import com.wafflestudio.waflog.domain.user.model.Likes
import com.wafflestudio.waflog.domain.user.model.Series
import com.wafflestudio.waflog.domain.user.model.User
import org.hibernate.annotations.Formula
import javax.persistence.*
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "url"])])
class Post(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @field:NotNull
    val user: User,

    @field:NotBlank
    val title: String,

    @Column(columnDefinition = "TEXT")
    val content: String,

    @field:Min(0)
    val views: Int = 0,

    @OneToMany(mappedBy = "likedPost")
    var likedUser: MutableList<Likes>,

    val thumbnail: String, // thumbnail image file url

    val summary: String,

    val private: Boolean,

    @Formula(
        value = "10 * views " +
            "+ 7 * (SELECT count(1) FROM likes l WHERE l.post_id = id) " +
            "+ 3 * (SELECT count(1) FROM comment c WHERE c.post_id = id)"
    )
    val trending: Int = 0,

    @field:NotBlank
    val url: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id", referencedColumnName = "id")
    val series: Series?,

    @OneToMany(mappedBy = "post")
    @OrderBy("root_comment ASC, lft ASC")
    var comments: MutableList<Comment>,

    @OneToMany(mappedBy = "post")
    var postTags: MutableList<PostTag>
) : BaseTimeEntity() {

    fun getPrevPost(): Post? {
        val userPosts: List<Post> = this.user.posts
        val userNotPrivatePosts: List<Post> = userPosts.filter { p -> !p.private }
        return userNotPrivatePosts.lastOrNull { it.id < this.id }
    }

    fun getNextPost(): Post? {
        val userPosts: List<Post> = this.user.posts
        val userNotPrivatePosts: List<Post> = userPosts.filter { p -> !p.private }
        return userNotPrivatePosts.firstOrNull { it.id > this.id }
    }
}
