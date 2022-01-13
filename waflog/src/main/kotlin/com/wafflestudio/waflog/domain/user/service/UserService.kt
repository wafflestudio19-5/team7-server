package com.wafflestudio.waflog.domain.user.service

import com.wafflestudio.waflog.domain.post.dto.PostDto
import com.wafflestudio.waflog.domain.post.model.Post
import com.wafflestudio.waflog.domain.user.dto.SeriesDto
import com.wafflestudio.waflog.domain.user.dto.UserDto
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
        userId: String,
        keyword: String?,
        pageable: Pageable
    ): Page<PostDto.PostInUserPostsResponse> {
        val user = userRepository.findByUserId(userId)
            ?: throw UserNotFoundException("There is no user id $userId")
        val searchedPosts = if (keyword == null || keyword == "") {
            user.posts
        } else {
            user.posts.filter { post -> postFilter(post, keyword) }
        }
        val searchedPostsResponse = searchedPosts.map { post -> PostDto.PostInUserPostsResponse(post) }
        val start: Int = pageable.offset.toInt()
        val end: Int = (start + pageable.pageSize).coerceAtMost(searchedPostsResponse.size)
        return PageImpl(
            searchedPostsResponse.subList(start, end),
            pageable,
            searchedPostsResponse.size.toLong()
        )
    }

    private fun postFilter(post: Post, keyword: String): Boolean {
        return post.title.contains(keyword) || post.content.contains(keyword)
    }
}
