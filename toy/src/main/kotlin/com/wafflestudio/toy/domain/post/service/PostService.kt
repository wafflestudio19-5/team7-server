package com.wafflestudio.toy.domain.post.service

import com.wafflestudio.toy.domain.post.dto.PostDto
import com.wafflestudio.toy.domain.post.model.Post
import com.wafflestudio.toy.domain.post.repository.PostRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class PostService(
    private val postRepository: PostRepository
) {
    fun getMainPagePosts(): List<PostDto.MainPageResponse> {
        val posts: List<Post> =
            postRepository.findAllByPrivateIsFalse(pageable = PageRequest.of(0, 30, Sort.Direction.DESC, "createdAt"))
        return posts.map { post -> PostDto.MainPageResponse(post) }
    }
}