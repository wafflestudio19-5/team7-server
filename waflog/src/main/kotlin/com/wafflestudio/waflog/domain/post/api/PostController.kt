package com.wafflestudio.waflog.domain.post.api

import com.wafflestudio.waflog.domain.post.dto.PostDto
import com.wafflestudio.waflog.domain.post.service.PostService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/post")
class PostController(
    private val postService: PostService
) {
    @GetMapping("/recent")
    @ResponseStatus(HttpStatus.OK)
    fun getRecentPosts(
        @PageableDefault(
            size = 30, sort = ["createdAt"], direction = Sort.Direction.DESC
        ) pageable: Pageable
    ): Page<PostDto.MainPageResponse> {
        return postService.getRecentPosts(pageable)
    }

    @GetMapping("/trend")
    @ResponseStatus(HttpStatus.OK)
    fun getTrendingPosts(
        @PageableDefault(
            size = 30, sort = ["trending"], direction = Sort.Direction.DESC
        ) pageable: Pageable,
        @RequestParam("date", required = false, defaultValue = "7") date: Int
    ): Page<PostDto.MainPageResponse> {
        return postService.getTrendingPosts(pageable, date)
    }

    @GetMapping("/{post_id}")
    @ResponseStatus(HttpStatus.OK)
    fun getPostDetail(@PathVariable("post_id") postId: Long): PostDto.PageDetailResponse {
        return postService.getPostDetail(postId)
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    fun searchPosts(
        @PageableDefault(
            size = 30, sort = ["trending"], direction = Sort.Direction.DESC
        ) pageable: Pageable,
        @RequestParam("keyword", required = true) keyword: String
    ): Page<PostDto.MainPageResponse> {
        return postService.searchPosts(pageable, keyword)
    }
}