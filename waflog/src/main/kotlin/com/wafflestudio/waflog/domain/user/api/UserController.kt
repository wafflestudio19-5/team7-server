package com.wafflestudio.waflog.domain.user.api

import com.wafflestudio.waflog.domain.post.dto.PostDto
import com.wafflestudio.waflog.domain.user.dto.SeriesDto
import com.wafflestudio.waflog.domain.user.dto.UserDto
import com.wafflestudio.waflog.domain.user.model.User
import com.wafflestudio.waflog.domain.user.service.UserService
import com.wafflestudio.waflog.global.auth.CurrentUser
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val userService: UserService
) {
    @PostMapping("/series")
    @ResponseStatus(HttpStatus.CREATED)
    fun addSeries(@RequestBody createRequest: SeriesDto.CreateRequest, @CurrentUser user: User) {
        userService.addSeries(createRequest, user)
    }

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    fun getSimpleInfo(@CurrentUser user: User): UserDto.SimpleResponse {
        return UserDto.SimpleResponse(user)
    }

    @GetMapping("/@{user_id}")
    @ResponseStatus(HttpStatus.OK)
    fun getUserDetail(@PathVariable("user_id") userId: String): UserDto.UserDetailResponse {
        return userService.getUserDetail(userId)
    }

    @GetMapping("/@{user_id}/search")
    @ResponseStatus(HttpStatus.OK)
    fun searchUserPosts(
        @PageableDefault(
            size = 30, sort = ["createdAt"], direction = Sort.Direction.DESC
        ) pageable: Pageable,
        @PathVariable("user_id") userId: String,
        @RequestParam("keyword", required = false) keyword: String?
    ): Page<PostDto.PostInUserPostsResponse> {
        return userService.searchUserPosts(userId, keyword, pageable)
    }
}
