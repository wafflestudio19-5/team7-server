package com.wafflestudio.waflog.domain.user.api

import com.wafflestudio.waflog.domain.post.dto.PostDto
import com.wafflestudio.waflog.domain.tag.dto.UserTagDto
import com.wafflestudio.waflog.domain.user.dto.SeriesDto
import com.wafflestudio.waflog.domain.user.dto.UserDto
import com.wafflestudio.waflog.domain.user.model.User
import com.wafflestudio.waflog.domain.user.service.UserService
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

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun withdrawUser(@CurrentUser user: User) {
        userService.withdrawUser(user)
    }

    @GetMapping("/@{user_id}")
    @ResponseStatus(HttpStatus.OK)
    fun getUserDetail(@PathVariable("user_id") userId: String): UserDto.UserDetailResponse {
        return userService.getUserDetail(userId)
    }

    @GetMapping("/@{user_id}/search")
    @ResponseStatus(HttpStatus.OK)
    fun searchUserPosts(
        @CurrentUser user: User?,
        @PageableDefault(
            size = 30, sort = ["createdAt"], direction = Sort.Direction.DESC
        ) pageable: Pageable,
        @PathVariable("user_id") userId: String,
        @RequestParam("keyword", required = false) keyword: String?
    ): Page<PostDto.PostInUserPostsResponse> {
        return userService.searchUserPosts(user, userId, keyword, pageable)
    }

    @GetMapping("/lists/liked")
    @ResponseStatus(HttpStatus.OK)
    fun getLikedPosts(
        @PageableDefault(size = 22) pageable: Pageable,
        @CurrentUser user: User
    ): Page<PostDto.MainPageResponse> {
        return userService.getLikedPosts(pageable, user)
    }

    @GetMapping("/lists/read")
    @ResponseStatus(HttpStatus.OK)
    fun getReadPosts(
        @PageableDefault(size = 22) pageable: Pageable,
        @CurrentUser user: User
    ): Page<PostDto.MainPageResponse> {
        return userService.getReadPosts(pageable, user)
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

    @GetMapping("/@{user_id}/series/@{series_name}")
    @ResponseStatus(HttpStatus.OK)
    fun getUserSeriesPosts(
        @PageableDefault(size = 30, sort = ["order"]) pageable: Pageable,
        @PathVariable("user_id") userId: String,
        @PathVariable("series_name") seriesName: String,
        @CurrentUser user: User?
    ): Page<PostDto.SeriesResponse> {
        return userService.getUserSeriesPosts(userId, seriesName, pageable, user)
    }

    @PutMapping("/series/@{series_name}")
    @ResponseStatus(HttpStatus.OK)
    fun putUserSeries(
        @PathVariable("series_name") seriesName: String,
        @RequestBody putRequest: SeriesDto.PutRequest,
        @CurrentUser user: User
    ) {
        return userService.putUserSeries(seriesName, putRequest, user)
    }

    @GetMapping("/@{user_id}/tags")
    @ResponseStatus(HttpStatus.OK)
    fun getUserTags(
        @PathVariable("user_id") userId: String,
        @CurrentUser user: User?
    ): ListResponse<UserTagDto> {
        return userService.getUserTags(userId, user)
    }

    @GetMapping("/@{user_id}/tag/{tag_url}")
    @ResponseStatus(HttpStatus.OK)
    fun getUserPostWithTag(
        @PageableDefault(
            size = 30, sort = ["createdAt"], direction = Sort.Direction.DESC
        ) pageable: Pageable,
        @PathVariable("user_id") userId: String,
        @PathVariable("tag_url") tagUrl: String,
        @CurrentUser user: User?
    ): Page<PostDto.PostInUserPostsResponse> {
        return userService.getUserTagPosts(pageable, userId, tagUrl, user)
    }

    @GetMapping("/setting")
    @ResponseStatus(HttpStatus.OK)
    fun getUserSetting(
        @CurrentUser user: User
    ): UserDto.UserSettingResponse {
        return UserDto.UserSettingResponse(user)
    }

    @PutMapping("/image")
    @ResponseStatus(HttpStatus.OK)
    fun updateUserImage(
        @RequestBody imageUpdateRequest: UserDto.ImageDto,
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
        @RequestBody profileDto: UserDto.ProfileDto,
        @CurrentUser user: User
    ): UserDto.ProfileDto {
        return userService.updateUserProfile(profileDto, user)
    }

    @PutMapping("/title")
    @ResponseStatus(HttpStatus.OK)
    fun updateUserPageTitle(
        @RequestBody titleDto: UserDto.TitleDto,
        @CurrentUser user: User
    ): UserDto.TitleDto {
        return userService.updateUserPageTitle(titleDto, user)
    }

    @PutMapping("/social")
    @ResponseStatus(HttpStatus.OK)
    fun updateUserSocialInfo(
        @RequestBody socialInfoDto: UserDto.SocialInfoDto,
        @CurrentUser user: User
    ): UserDto.SocialInfoDto {
        return userService.updateUserSocialInfo(socialInfoDto, user)
    }
}
