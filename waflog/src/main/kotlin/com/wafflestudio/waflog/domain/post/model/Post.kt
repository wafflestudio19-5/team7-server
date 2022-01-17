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
    var title: String,

    @Column(columnDefinition = "TEXT")
    var content: String,

    @field:Min(0)
    val views: Int = 0,

    @OneToMany(mappedBy = "likedPost")
    var likedUser: MutableList<Likes>,

    var thumbnail: String, // thumbnail image file url

    var summary: String,

    var private: Boolean,

    @Formula(
        value = "10 * views " +
            "+ 7 * (SELECT count(1) FROM likes l WHERE l.post_id = id) " +
            "+ 3 * (SELECT count(1) FROM comment c WHERE c.post_id = id)"
    )
    val trending: Int = 0,

    @field:NotBlank
    var url: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id", referencedColumnName = "id")
    var series: Series?,

    @OneToMany(mappedBy = "post")
    @OrderBy("root_comment ASC, lft ASC")
    var comments: MutableList<Comment>,

    @OneToMany(mappedBy = "post")
    var postTags: MutableList<PostTag>
) : BaseTimeEntity() {

    fun getPrevPost(user: User?): Post? {
        return this.user.posts.filter { p -> !p.private || this.user == user }.lastOrNull { it.id < this.id }
    }

    fun getNextPost(user: User?): Post? {
        return this.user.posts.filter { p -> !p.private || this.user == user }.firstOrNull { it.id > this.id }
    }
}
