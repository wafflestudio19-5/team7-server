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

    @GetMapping("/@{user_id}/about")
    @ResponseStatus(HttpStatus.OK)
    fun getUserLongIntro(@PathVariable("user_id") userId: String): UserDto.UserLongIntroResponse {
        return userService.getUserLongIntro(userId)
    }

    @GetMapping("/@{user_id}/series")
    @ResponseStatus(HttpStatus.OK)
    fun getUserSeries(
        @PageableDefault(
            size = 30, sort = ["createdAt"], direction = Sort.Direction.DESC
        ) pageable: Pageable,
        @PathVariable("user_id") userId: String
    ): Page<SeriesDto.SimpleResponse> {
        return userService.getUserSeries(userId, pageable)
    }

    @GetMapping("/setting")
    @ResponseStatus(HttpStatus.OK)
    fun getUserSetting(
        @CurrentUser user: User
    ): UserDto.UserDetailResponse {
        return UserDto.UserDetailResponse(user)
    }

    @PutMapping("/image")
    @ResponseStatus(HttpStatus.OK)
    fun updateUserImage(
        imageUpdateRequest: UserDto.ImageDto,
        @CurrentUser user: User
    ): UserDto.ImageDto {
        return userService.updateUserImage(imageUpdateRequest, user)
    }

    @DeleteMapping("/image")
    @ResponseStatus(HttpStatus.OK)
    fun deleteUserImage(@CurrentUser user: User): UserDto.ImageDto {
        return userService.deleteUserImage(user)
    }

    @PutMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    fun updateUserProfile(
        profileDto: UserDto.ProfileDto,
        @CurrentUser user: User
    ): UserDto.ProfileDto {
        return userService.updateUserProfile(profileDto, user)
    }

    @PutMapping("/title")
    @ResponseStatus(HttpStatus.OK)
    fun updateUserPageTitle(
        titleDto: UserDto.TitleDto,
        @CurrentUser user: User
    ): UserDto.TitleDto {
        return userService.updateUserPageTitle(titleDto, user)
    }

    @PutMapping("/social")
    @ResponseStatus(HttpStatus.OK)
    fun updateUserSocialInfo(
        socialInfoDto: UserDto.SocialInfoDto,
        @CurrentUser user: User
    ): UserDto.SocialInfoDto {
        return userService.updateUserSocialInfo(socialInfoDto, user)
    }
}
