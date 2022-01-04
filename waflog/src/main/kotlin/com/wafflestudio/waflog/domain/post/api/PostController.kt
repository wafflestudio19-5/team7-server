package com.wafflestudio.waflog.domain.post.api

import com.wafflestudio.waflog.domain.post.dto.PostDto
import com.wafflestudio.waflog.domain.post.exception.InvalidPostFormException
import com.wafflestudio.waflog.domain.post.model.Post
import com.wafflestudio.waflog.domain.post.repository.PostRepository
import com.wafflestudio.waflog.domain.post.service.PostService
import com.wafflestudio.waflog.domain.user.model.User
import com.wafflestudio.waflog.domain.user.repository.SeriesRepository
import com.wafflestudio.waflog.global.auth.CurrentUser
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/post")
class PostController(
    private val postService: PostService,
    private val postRepository: PostRepository,
    private val seriesRepository: SeriesRepository
) {
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun writePost(@RequestBody createRequest: PostDto.CreateRequest, @CurrentUser user: User) {
        val title = createRequest.title
        if (title == "") throw InvalidPostFormException("제목이 비어있습니다.")
        val content = createRequest.content
        val thumbnail = createRequest.thumbnail
        val summary = createRequest.summary
        val private = createRequest.private
        val url = createRequest.url
        val seriesName = createRequest.seriesName
        val series = seriesRepository.findByName(seriesName)
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

    @GetMapping("/{post_id}")
    @ResponseStatus(HttpStatus.OK)
    fun getPostDetail(@PathVariable("post_id") postId: Long): PostDto.PageDetailResponse {
        return postService.getPostDetail(postId)
    }

    @GetMapping("/@{user_id}/{post_url}")
    @ResponseStatus(HttpStatus.OK)
    fun getPostDetailWithURL(
        @PathVariable("user_id") userId: String,
        @PathVariable("post_url") postURL: String,
    ): PostDto.PageDetailResponse {
        return postService.getPostDetailWithURL(userId, postURL)
    }
}
