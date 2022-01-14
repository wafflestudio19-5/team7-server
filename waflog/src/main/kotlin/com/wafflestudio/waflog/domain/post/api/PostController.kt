package com.wafflestudio.waflog.domain.post.api

import com.wafflestudio.waflog.domain.post.dto.CommentDto
import com.wafflestudio.waflog.domain.post.dto.PostDto
import com.wafflestudio.waflog.domain.post.service.PostService
import com.wafflestudio.waflog.domain.user.model.User
import com.wafflestudio.waflog.global.auth.CurrentUser
import com.wafflestudio.waflog.global.common.dto.ListResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/post")
class PostController(
    private val postService: PostService
) {
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun writePost(@RequestBody createRequest: PostDto.CreateRequest, @CurrentUser user: User) {
        postService.writePost(createRequest, user)
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
    fun getPostDetail(
        @PathVariable("post_id") postId: Long,
        @CurrentUser user: User?
    ): PostDto.PageDetailResponse {
        return postService.getPostDetail(postId, user)
    }

    @GetMapping("/@{user_id}/{post_url}")
    @ResponseStatus(HttpStatus.OK)
    fun getPostDetailWithURL(
        @PathVariable("user_id") userId: String,
        @PathVariable("post_url") postURL: String,
        @CurrentUser user: User?
    ): PostDto.PageDetailResponse {
        return postService.getPostDetailWithURL(userId, postURL, user)
    }

    @PostMapping("/{post_id}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    fun writeComment(
        @PathVariable("post_id") postId: Long,
        @RequestBody createRequest: CommentDto.CreateRequest,
        @CurrentUser user: User
    ): ListResponse<CommentDto.RootCommentResponse> {
        return postService.writeComment(postId, createRequest, user)
    }

    @PutMapping("/{post_id}/comment/{comment_id}")
    @ResponseStatus(HttpStatus.OK)
    fun modifyComment(
        @PathVariable("post_id") postId: Long,
        @PathVariable("comment_id") commentId: Long,
        @RequestBody modifyRequest: CommentDto.ModifyRequest,
        @CurrentUser user: User
    ): ListResponse<CommentDto.RootCommentResponse> {
        return postService.modifyComment(postId, commentId, modifyRequest, user)
    }

    @DeleteMapping("/{post_id}/comment/{comment_id}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteComment(
        @PathVariable("post_id") postId: Long,
        @PathVariable("comment_id") commentId: Long,
        @CurrentUser user: User
    ): ListResponse<CommentDto.RootCommentResponse> {
        return postService.deleteComment(postId, commentId, user)
    }

    @PostMapping("/{post_id}/like")
    @ResponseStatus(HttpStatus.OK)
    fun likeOrDislikePost(
        @PathVariable("post_id") postId: Long,
        @CurrentUser user: User
    ): PostDto.PostLikesResponse {
        return postService.addLikeInPost(postId, user)
    }

    @GetMapping("/{post_id}/like/current")
    @ResponseStatus(HttpStatus.OK)
    fun isLikedPost(
        @PathVariable("post_id") postId: Long,
        @CurrentUser user: User?
    ): Boolean {
        return user?.let { postService.isLikedPost(postId, it) } ?: false
    }
}
