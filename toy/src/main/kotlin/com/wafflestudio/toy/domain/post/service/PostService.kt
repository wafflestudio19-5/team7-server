package com.wafflestudio.toy.domain.post.service

import com.wafflestudio.toy.domain.post.dto.PostDto
import com.wafflestudio.toy.domain.post.model.Post
import com.wafflestudio.toy.domain.post.repository.PostRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class PostService(
    private val postRepository: PostRepository
) {
    fun getRecentPosts(pageable : Pageable): Page<PostDto.MainPageResponse> {
        val posts: Page<Post> =
            postRepository.findAllByPrivateIsFalse(pageable)
        return posts.map { post -> PostDto.MainPageResponse(post) }
    }

    fun getTrendingPosts(pageable: Pageable, date: Int): Page<PostDto.MainPageResponse> {
        val dateStart = LocalDate.now().atStartOfDay().minusDays(date.toLong())
        val posts = postRepository.findAllByPrivateIsFalseAndCreatedAtAfter(pageable, dateStart)
        return posts.map { post -> PostDto.MainPageResponse(post) }
    }

    fun searchPosts(pageable: Pageable, keyword: String): Page<PostDto.MainPageResponse> {
        val posts = postRepository.searchByKeyword(pageable, keyword, keyword, keyword)
        return posts.map { post -> PostDto.MainPageResponse(post) }
    }
}
