package com.wafflestudio.waflog.domain.post.dto

import com.wafflestudio.waflog.domain.image.dto.ImageDto
import com.wafflestudio.waflog.domain.post.model.Comment
import com.wafflestudio.waflog.domain.post.model.Post
import com.wafflestudio.waflog.domain.tag.dto.TagDto
import com.wafflestudio.waflog.domain.user.dto.UserDto
import com.wafflestudio.waflog.domain.user.model.User
import com.wafflestudio.waflog.global.common.dto.ListResponse
import java.time.LocalDateTime

class PostDto {
    data class MainPageResponse(
        val id: Long,
        val user: UserDto.SimpleResponse,
        val title: String,
        val url: String,
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
            url = post.url,
            thumbnail = post.thumbnail,
            summary = post.summary,
            createAt = post.createdAt,
            likes = post.likedUser.size,
            comments = post.comments.size
        )
    }

    data class SearchResultResponse(
        val id: Long,
        val user: UserDto.SimpleResponse,
        val title: String,
        val url: String,
        val tags: List<TagDto.TagResponse>,
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
            url = post.url,
            tags = post.postTags.map { TagDto.TagResponse(it.tag) },
            thumbnail = post.thumbnail,
            summary = post.summary,
            createAt = post.createdAt,
            likes = post.likedUser.size,
            comments = post.comments.size
        )
    }

    data class PageDetailResponse(
        val id: Long,
        val user: UserDto.UserInPostDetailResponse,
        val url: String,
        val seriesName: String?,
        val seriesPosts: List<IdAndTitleResponse>?,
        val title: String,
        val content: String,
        val likes: Int,
        val thumbnail: String,
        val tags: List<TagDto.TagInPostResponse>,
        val comments: ListResponse<CommentDto.RootCommentResponse>,
        val prevPost: IdAndTitleResponse?,
        val nextPost: IdAndTitleResponse?,
        val createdAt: LocalDateTime?
    ) {
        constructor(post: Post, user: User?) : this(
            id = post.id,
            user = UserDto.UserInPostDetailResponse(post.user),
            url = post.url,
            seriesName = post.series?.name,
            seriesPosts = post.series?.posts?.map { p -> IdAndTitleResponse(p) },
            title = post.title,
            content = post.content,
            likes = post.likedUser.size,
            thumbnail = post.thumbnail,
            tags = post.postTags.map { postTag -> TagDto.TagInPostResponse(postTag.tag) },
            comments = getCommentListResponse(post.comments),
            prevPost = post.getPrevPost(user)?.let { IdAndTitleResponse(it) },
            nextPost = post.getNextPost(user)?.let { IdAndTitleResponse(it) },
            createdAt = post.createdAt
        )
    }

    data class IdAndTitleResponse(
        val id: Long,
        val title: String,
        val url: String
    ) {
        constructor(post: Post) : this(
            id = post.id,
            title = post.title,
            url = post.url
        )
    }

    data class CreateRequest(
        val title: String,
        val content: String,
        val images: List<ImageDto.S3Token>,
        val tags: List<String> = listOf(),
        val thumbnail: String,
        val summary: String,
        val private: Boolean,
        val url: String,
        val seriesName: String?
    )

    data class PutRequest(
        val token: String,
        val title: String,
        val tags: List<String> = listOf(),
        val content: String,
        val images: List<ImageDto.S3Token>,
        val thumbnail: String,
        val summary: String,
        val private: Boolean,
        val url: String,
        val seriesName: String?
    )

    data class PostLikesResponse(
        val id: Long,
        val likes: Int,
        val isLiked: Boolean
    ) {
        constructor(post: Post, isLiked: Boolean) : this(
            id = post.id,
            likes = post.likedUser.size,
            isLiked = isLiked
        )
    }

    companion object {
        fun getCommentListResponse(comments: List<Comment>):
            ListResponse<CommentDto.RootCommentResponse> {

            val rootComments = comments.filter { it.depth == 0 }
            val replies = comments.filter { it.depth > 0 }

            return ListResponse(
                comments.size,
                rootComments
                    .map { root ->
                        CommentDto.RootCommentResponse(
                            root,
                            replies.filter { it.rootComment == root.id }
                        )
                    }
            )
        }
    }

    data class PostInUserPostsResponse(
        val id: Long,
        val title: String,
        val private: Boolean,
        val url: String,
        val thumbnail: String, // thumbnail file url
        val summary: String,
        val tags: List<TagDto.TagInPostResponse>,
        val createAt: LocalDateTime?,
        val comments: Int // num of comments
    ) {
        constructor(post: Post) : this(
            id = post.id,
            title = post.title,
            private = post.private,
            url = post.url,
            thumbnail = post.thumbnail,
            summary = post.summary,
            tags = post.postTags.map { postTag -> TagDto.TagInPostResponse(postTag.tag) },
            createAt = post.createdAt,
            comments = post.comments.size
        )
    }

    data class SeriesResponse(
        val order: Int,
        val title: String,
        val thumbnail: String,
        val url: String,
        val createAt: LocalDateTime?
    ) {
        constructor(post: Post) : this(
            order = post.seriesOrder!!,
            title = post.title,
            thumbnail = post.thumbnail,
            url = post.url,
            createAt = post.createdAt
        )
    }
}
