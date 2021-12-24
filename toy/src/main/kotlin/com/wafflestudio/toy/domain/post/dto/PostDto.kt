package com.wafflestudio.toy.domain.post.dto

import com.wafflestudio.toy.domain.post.model.Post
import com.wafflestudio.toy.domain.user.dto.UserDto
import java.time.LocalDateTime

class PostDto {
    data class MainPageResponse(
        val user: UserDto.SimpleResponse,
        val title: String,
        val thumbnail: String, // thumbnail file url
        val summary: String,
        val createAt: LocalDateTime?,
        val likes: Int,         // num of likes
        val comments: Int      // num of comments
    ) {
        constructor(post: Post) : this(
            user = UserDto.SimpleResponse(post.user),
            title = post.title,
            thumbnail = post.thumbnail,
            summary = post.summary,
            createAt = post.createdAt,
            likes = post.likes,
            comments = post.comments.size
        )
    }
}
