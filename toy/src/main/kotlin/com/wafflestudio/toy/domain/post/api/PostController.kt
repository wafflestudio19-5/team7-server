package com.wafflestudio.toy.domain.post.api

import com.wafflestudio.toy.domain.post.dto.PostDto
import com.wafflestudio.toy.domain.post.service.PostService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/post")
class PostController(
    private val postService: PostService
) {
    @GetMapping("/recent/")
    @ResponseStatus(HttpStatus.OK)
    fun getRecentPost(): List<PostDto.MainPageResponse> {
        return postService.getMainPagePosts()
    }
}