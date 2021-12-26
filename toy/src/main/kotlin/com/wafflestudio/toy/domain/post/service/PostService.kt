package com.wafflestudio.toy.domain.post.service

import com.wafflestudio.toy.domain.post.dto.PostDto
import com.wafflestudio.toy.domain.post.exception.PostNotFoundException
import com.wafflestudio.toy.domain.post.model.Post
import com.wafflestudio.toy.domain.post.repository.PostRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDate
import javax.persistence.EntityNotFoundException

@Service
class PostService(
    private val postRepository: PostRepository
) {
    fun getRecentPosts(pageable: Pageable): List<PostDto.MainPageResponse> {
        val posts: List<Post> =
            postRepository.findAllByPrivateIsFalse(pageable)
        return posts.map { post -> PostDto.MainPageResponse(post) }
    }

    fun getTrendingPosts(pageable: Pageable, date: Int): List<PostDto.MainPageResponse> {
        val dateStart = LocalDate.now().atStartOfDay().minusDays(date.toLong())
        val posts = postRepository.findAllByPrivateIsFalseAndCreatedAtAfter(pageable, dateStart)
        return posts.map { post -> PostDto.MainPageResponse(post) }
    }

    fun getPostDetail(id: Long): PostDto.PageDetailResponse {
        val post = postRepository.findByIdOrNull(id)
        return if(post !=null) { PostDto.PageDetailResponse(post) }
        else { throw PostNotFoundException("There is no post id $id") }
    }
}
