package com.wafflestudio.waflog.domain.user.service

import com.wafflestudio.waflog.domain.post.dto.PostDto
import com.wafflestudio.waflog.domain.post.model.Post
import com.wafflestudio.waflog.domain.user.dto.SeriesDto
import com.wafflestudio.waflog.domain.user.dto.UserDto
import com.wafflestudio.waflog.domain.user.exception.InvalidUserNameException
import com.wafflestudio.waflog.domain.user.exception.InvalidUserPageTitleException
import com.wafflestudio.waflog.domain.user.exception.SeriesUrlExistException
import com.wafflestudio.waflog.domain.user.exception.UserNotFoundException
import com.wafflestudio.waflog.domain.user.model.Series
import com.wafflestudio.waflog.domain.user.model.User
import com.wafflestudio.waflog.domain.user.repository.SeriesRepository
import com.wafflestudio.waflog.domain.user.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val seriesRepository: SeriesRepository
) {
    fun addSeries(createRequest: SeriesDto.CreateRequest, user: User) {
        val seriesName = createRequest.name
        seriesRepository.findByNameAndUser(seriesName, user)
            ?: run {
                val newSeries = Series(user, seriesName, mutableListOf())
                seriesRepository.save(newSeries)
                return
            }
        throw SeriesUrlExistException("이미 존재하는 URL입니다.")
    }

    fun getUserDetail(userId: String): UserDto.UserDetailResponse {
        val user = userRepository.findByUserId(userId)
            ?: throw UserNotFoundException("There is no user id $userId")
        return UserDto.UserDetailResponse(user)
    }

    fun searchUserPosts(
        user: User?,
        userId: String,
        keyword: String?,
        pageable: Pageable
    ): Page<PostDto.PostInUserPostsResponse> {
        val targetUser = userRepository.findByUserId(userId)
            ?: throw UserNotFoundException("There is no user id $userId")
        var searchedPosts: List<Post> = if (targetUser == user) { // my posts
            if (keyword == null || keyword == "") {
                targetUser.posts // all posts
            } else {
                targetUser.posts.filter { post -> postFilter(post, keyword) } // searched posts
            }
        } else { // other's posts
            if (keyword == null || keyword == "") {
                targetUser.posts.filter { post -> !post.private } // all posts
            } else {
                targetUser.posts.filter { post -> postFilter(post, keyword) && !post.private } // searched posts
            }
        }
        val searchedPostsResponse = searchedPosts.map { post -> PostDto.PostInUserPostsResponse(post) }
        return makePage(pageable, searchedPostsResponse)
    }

    private fun postFilter(post: Post, keyword: String): Boolean {
        return (post.title.contains(keyword) || post.content.contains(keyword)) && !post.private
    }

    fun getUserLongIntro(userId: String): UserDto.UserLongIntroResponse {
        val user = userRepository.findByUserId(userId)
            ?: throw UserNotFoundException("There is no user id $userId")
        return UserDto.UserLongIntroResponse(user)
    }

    fun getUserSeries(userId: String, pageable: Pageable): Page<SeriesDto.SimpleResponse> {
        val user = userRepository.findByUserId(userId)
            ?: throw UserNotFoundException("There is no user id $userId")
        val userSeries = user.series.map { series -> SeriesDto.SimpleResponse(series) }
        return makePage(pageable, userSeries)
    }

    fun updateUserImage(
        imageUpdateRequest: UserDto.ImageDto,
        user: User
    ): UserDto.ImageDto {
        user.image = imageUpdateRequest.image
        return UserDto.ImageDto(userRepository.save(user).image)
    }

    fun deleteUserImage(user: User): UserDto.ImageDto {
        user.image =
            "https://wafflestudio.com/_next/image?url=%2Fimages%2Ficon_intro.svg&w=640&q=75"
        return UserDto.ImageDto(userRepository.save(user).image)
    }

    fun updateUserProfile(
        profileDto: UserDto.ProfileDto,
        user: User
    ): UserDto.ProfileDto {
        val name = profileDto.name
        val shortIntro = profileDto.shortIntro
        if (name.isEmpty()) throw InvalidUserNameException("User name must not be empty")

        user.name = name
        user.shortIntro = shortIntro

        return UserDto.ProfileDto(userRepository.save(user))
    }

    fun updateUserPageTitle(
        titleDto: UserDto.TitleDto,
        user: User
    ): UserDto.TitleDto {
        val pageTitle = titleDto.title
        if (pageTitle.isEmpty()) throw InvalidUserPageTitleException("User page title must not be empty")

        user.pageTitle = pageTitle

        return UserDto.TitleDto(userRepository.save(user))
    }

    fun updateUserSocialInfo(
        socialInfoDto: UserDto.SocialInfoDto,
        user: User
    ): UserDto.SocialInfoDto {
        user.publicEmail = socialInfoDto.publicEmail
        user.githubId = socialInfoDto.githubId
        user.facebookId = socialInfoDto.facebookId
        user.twitterId = socialInfoDto.twitterId
        user.homepage = socialInfoDto.homepage

        return UserDto.SocialInfoDto(userRepository.save(user))
    }

    private fun <T> makePage(pageable: Pageable, contents: List<T>): Page<T> {
        val start: Int = pageable.offset.toInt()
        val end: Int = (start + pageable.pageSize).coerceAtMost(contents.size)
        return PageImpl(
            contents.subList(start, end),
            pageable,
            contents.size.toLong()
        )
    }
}
