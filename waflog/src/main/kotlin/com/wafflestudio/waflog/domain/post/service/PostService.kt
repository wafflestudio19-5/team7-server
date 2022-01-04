package com.wafflestudio.waflog.domain.post.service

import com.wafflestudio.waflog.domain.post.dto.PostDto
import com.wafflestudio.waflog.domain.post.exception.PostNotFoundException
import com.wafflestudio.waflog.domain.post.model.Post
import com.wafflestudio.waflog.domain.post.repository.PostRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class PostService(
    private val postRepository: PostRepository
) {
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
}
