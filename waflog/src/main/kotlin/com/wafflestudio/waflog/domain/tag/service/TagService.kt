package com.wafflestudio.waflog.domain.tag.service

import com.wafflestudio.waflog.domain.post.dto.PostDto
import com.wafflestudio.waflog.domain.post.repository.PostRepository
import com.wafflestudio.waflog.domain.tag.dto.TagDto
import com.wafflestudio.waflog.domain.tag.exception.TagNotFoundException
import com.wafflestudio.waflog.domain.tag.repository.TagRepository
import com.wafflestudio.waflog.domain.user.model.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class TagService(
    private val tagRepository: TagRepository,
    private val postRepository: PostRepository
) {
    fun getTags(pageable: Pageable): Page<TagDto.TagResponse> {
        val tags = tagRepository.findAll(pageable)
        return tags.map { TagDto.TagResponse(it) }
    }

    fun getPostsWithTag(
        pageable: Pageable,
        tagUrl: String,
        user: User?
    ): Page<PostDto.SearchResultResponse> {
        val tag = tagRepository.findByUrl(tagUrl)
            ?: throw TagNotFoundException("Tag with url <$tagUrl> does not exist")

        val posts = user?.let {
            postRepository.searchByTagOnLoggedIn(pageable, tag.id, it.userId)
        } ?: run {
            postRepository.searchByTagOnLoggedOut(pageable, tag.id)
        }

        return posts.map { PostDto.SearchResultResponse(it) }
    }
}
