package com.wafflestudio.waflog.domain.post.dto

import com.wafflestudio.waflog.domain.post.model.Post
import com.wafflestudio.waflog.domain.tag.dto.TagDto
import com.wafflestudio.waflog.domain.user.dto.UserDto
import java.time.LocalDateTime

class PostDto {
    data class MainPageResponse(
        val id: Long,
        val user: UserDto.SimpleResponse,
        val title: String,
        val thumbnail: String, // thumbnail file url
        val summary: String,
        val createAt: LocalDateTime?,
        val likes: Int, // num of likes
        val comments: Int // num of comments
    ) {
        constructor(post: Post) : this(
            id = post.id,
            user = UserDto.SimpleResponse(post.user),
            title = post.title,
            thumbnail = post.thumbnail,
            summary = post.summary,
            createAt = post.createdAt,
            likes = post.likes,
            comments = post.comments.size
        )
    }

    data class PageDetailResponse(
        val id: Long,
        val user: UserDto.UserInPostDetailResponse,
        val url: String,
        val seriesPosts: List<IdAndTitleResponse>?,
        val content: String,
        val likes: Int,
        val tags: List<TagDto.TagResponse>,
        val prevPost: IdAndTitleResponse?,
        val nextPost: IdAndTitleResponse?
    ) {
        constructor(post: Post) : this(
            id = post.id,
            user = UserDto.UserInPostDetailResponse(post.user),
            url = post.url,
            seriesPosts = post.series?.posts?.map { p -> IdAndTitleResponse(p) },
            content = post.content,
            likes = post.likes,
            tags = post.postTags.map { postTag -> TagDto.TagResponse(postTag.tag) },
            prevPost = post.getPrevPost()?.let { IdAndTitleResponse(it) },
            nextPost = post.getNextPost()?.let { IdAndTitleResponse(it) }
        )
    }

    data class IdAndTitleResponse(
        val id: Long,
        val title: String
    ) {
        constructor(post: Post) : this(
            id = post.id,
            title = post.title
        )
    }
}
