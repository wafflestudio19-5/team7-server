package com.wafflestudio.waflog.domain.post.service

import com.wafflestudio.waflog.domain.post.dto.PostDto
import com.wafflestudio.waflog.domain.post.exception.InvalidPostFormException
import com.wafflestudio.waflog.domain.post.exception.PostNotFoundException
import com.wafflestudio.waflog.domain.post.model.Post
import com.wafflestudio.waflog.domain.post.repository.PostRepository
import com.wafflestudio.waflog.domain.user.exception.SeriesNotFoundException
import com.wafflestudio.waflog.domain.user.model.User
import com.wafflestudio.waflog.domain.user.repository.SeriesRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.*

@Service
class PostService(
    private val postRepository: PostRepository,
    private val seriesRepository: SeriesRepository
) {
    fun writePost(createRequest: PostDto.CreateRequest, user: User) {
        val title = createRequest.title
        if (title == "") throw InvalidPostFormException("제목이 비어있습니다.")
        val content = createRequest.content
        val thumbnail = createRequest.thumbnail
        val summary = createRequest.summary
        val private = createRequest.private
        var url = createRequest.url
        if (url == "") {
            if (title.length == 1) url = title + "-" + getRandomString(8)
            else url = title
        }
        val seriesName = createRequest.seriesName
        val series = seriesRepository.findByName(seriesName)
            ?: throw SeriesNotFoundException("series not found")
        val post = Post(
            user = user,
            title = title,
            content = content,
            likes = 0,
            thumbnail = thumbnail,
            summary = summary,
            private = private,
            url = url,
            series = series,
            comments = mutableListOf(),
            postTags = mutableListOf()
        )

        postRepository.save(post)
    }

    fun getRecentPosts(pageable: Pageable): Page<PostDto.MainPageResponse> {
        val posts: Page<Post> =
            postRepository.findAllByPrivateIsFalse(pageable)
        return posts.map { post -> PostDto.MainPageResponse(post) }
    }

    fun getTrendingPosts(pageable: Pageable, date: Int): Page<PostDto.MainPageResponse> {
        val dateStart = LocalDate.now().atStartOfDay().minusDays(date.toLong())
        val posts = postRepository.findAllByPrivateIsFalseAndCreatedAtAfter(pageable, dateStart)
        return posts.map { post -> PostDto.MainPageResponse(post) }
    }

    fun getPostDetail(id: Long): PostDto.PageDetailResponse {
        val post = postRepository.findByIdOrNull(id)
        post?.let { p -> return PostDto.PageDetailResponse(p) }
        throw PostNotFoundException("There is no post id $id")
    }

    fun searchPosts(pageable: Pageable, keyword: String): Page<PostDto.MainPageResponse> {
        val posts = postRepository.searchByKeyword(pageable, keyword, keyword, keyword)
        return posts.map { post -> PostDto.MainPageResponse(post) }
    }

    fun getPostDetailWithURL(userId: String, postURL: String): PostDto.PageDetailResponse {
        val post = postRepository.findByPrivateIsFalseAndUser_UserIdAndUrl(userId, postURL)
            ?: throw PostNotFoundException("There is no post with url '@$userId/$postURL'")
        return PostDto.PageDetailResponse(post)
    }

    private fun getRandomString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }
}
