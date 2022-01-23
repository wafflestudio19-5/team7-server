package com.wafflestudio.waflog.domain.user.service

import com.wafflestudio.waflog.domain.image.service.ImageService
import com.wafflestudio.waflog.domain.post.dto.PostDto
import com.wafflestudio.waflog.domain.post.model.Post
import com.wafflestudio.waflog.domain.post.repository.CommentRepository
import com.wafflestudio.waflog.domain.post.repository.PostRepository
import com.wafflestudio.waflog.domain.post.repository.PostTokenRepository
import com.wafflestudio.waflog.domain.save.repository.SaveRepository
import com.wafflestudio.waflog.domain.save.repository.SaveTokenRepository
import com.wafflestudio.waflog.domain.tag.repository.PostTagRepository
import com.wafflestudio.waflog.domain.tag.repository.TagRepository
import com.wafflestudio.waflog.domain.user.dto.SeriesDto
import com.wafflestudio.waflog.domain.user.dto.UserDto
import com.wafflestudio.waflog.domain.user.exception.InvalidPublicEmailException
import com.wafflestudio.waflog.domain.user.exception.InvalidUserNameException
import com.wafflestudio.waflog.domain.user.exception.InvalidUserPageTitleException
import com.wafflestudio.waflog.domain.user.exception.SeriesNotFoundException
import com.wafflestudio.waflog.domain.user.exception.SeriesUrlExistException
import com.wafflestudio.waflog.domain.user.exception.UserNotFoundException
import com.wafflestudio.waflog.domain.user.model.Series
import com.wafflestudio.waflog.domain.user.model.User
import com.wafflestudio.waflog.domain.user.repository.LikesRepository
import com.wafflestudio.waflog.domain.user.repository.ReadsRepository
import com.wafflestudio.waflog.domain.user.repository.SeriesRepository
import com.wafflestudio.waflog.domain.user.repository.UserRepository
import com.wafflestudio.waflog.global.auth.repository.VerificationTokenRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.TransactionSystemException

@Service
class UserService(
    private val userRepository: UserRepository,
    private val seriesRepository: SeriesRepository,
    private val postRepository: PostRepository,
    private val imageService: ImageService,
    private val postTokenRepository: PostTokenRepository,
    private val readsRepository: ReadsRepository,
    private val postTagRepository: PostTagRepository,
    private val tagRepository: TagRepository,
    private val likesRepository: LikesRepository,
    private val verificationTokenRepository: VerificationTokenRepository,
    private val commentRepository: CommentRepository,
    private val saveTokenRepository: SaveTokenRepository,
    private val saveRepository: SaveRepository
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
        val searchedPosts: List<Post> = targetUser.posts.filter {
            postFilter(it, keyword) && (!it.private || it.user == user)
        }
        val searchedPostsResponse = searchedPosts.map { post -> PostDto.PostInUserPostsResponse(post) }
        return makePage(pageable, searchedPostsResponse)
    }

    fun getLikedPosts(pageable: Pageable, user: User): Page<PostDto.MainPageResponse> {
        return user.likedPosts.filter { !it.likedPost.private || it.user == user }
            .map { PostDto.MainPageResponse(it.likedPost) }
            .let { makePage(pageable, it) }
    }

    fun getReadPosts(pageable: Pageable, user: User): Page<PostDto.MainPageResponse> {
        return user.readPosts.filter { !it.readPost.private || it.user == user }
            .map { PostDto.MainPageResponse(it.readPost) }
            .let { makePage(pageable, it) }
    }

    private fun postFilter(post: Post, keyword: String?): Boolean {
        return keyword?.let {
            post.title.contains(it) || post.content.contains(it)
        } ?: true
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

    fun getUserSeriesPosts(
        userId: String,
        seriesName: String,
        pageable: Pageable,
        user: User?
    ): Page<PostDto.SeriesResponse> {
        val seriesPosts = userRepository.findByUserId(userId)
            ?.let {
                seriesRepository.findByNameAndUser(seriesName, it)?.posts
                    ?: throw SeriesNotFoundException("There is no series with user id <$userId> and name <$seriesName>")
            }
            ?.filter { !it.private || it.user == user }
            ?.map { PostDto.SeriesResponse(it) }
            ?: throw UserNotFoundException("There is no user id $userId")
        return makePage(pageable, seriesPosts)
    }

    fun putUserSeries(
        seriesName: String,
        putRequest: SeriesDto.PutRequest,
        user: User
    ) {
        val name = putRequest.name
        if (name != seriesName)
            seriesRepository.findByNameAndUser(name, user)
                ?.also { throw SeriesUrlExistException("이미 존재하는 URL입니다.") }
        val putList: List<Pair<String, Int>> = putRequest.putOrder.toSortedMap().toList()
        seriesRepository.findByNameAndUser(seriesName, user)
            ?.apply { this.name = name }
            ?.also { seriesRepository.save(it) }?.posts
            ?.zip(putList)
            ?.map {
                it.first.seriesOrder = it.second.second
                postRepository.save(it.first)
            }
            ?: throw SeriesNotFoundException("There is no series with user id <${user.userId}> and name <$seriesName>")
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

        val updatedUser: User

        try {
            updatedUser = userRepository.save(user)
        } catch (e: TransactionSystemException) {
            throw InvalidPublicEmailException("Public email is invalid")
        }

        return UserDto.SocialInfoDto(updatedUser)
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

    fun withdrawUser(user: User) {
        // delete user's images
        imageService.removeAllUserImages(user)

        // delete user's post
        postTokenRepository.deleteAllMappingByUserId(user.id)
        likesRepository.deleteMappingByUserId(user.id)
        readsRepository.deleteMappingByUserId(user.id)
        commentRepository.deleteAllCommentMappingByUserId(user.id)
        postTagRepository.deleteMappingByUserId(user.id)
        postRepository.deleteAllUserPosts(user.id)
        tagRepository.deleteUnusedTags()

        // delete user's saves
        saveTokenRepository.deleteAllMappingByUserId(user.id)
        saveRepository.deleteAllUserSaves(user.id)

        commentRepository.updateCommentWriterByNull(user.id) // update user's comment to null's comment
        likesRepository.deleteAllByUserId(user.id) // delete all likes by user
        verificationTokenRepository.findByEmail(user.email)?.let {
            verificationTokenRepository.deleteById(it.id) // delete user token
        }
        userRepository.deleteById(user.id) // delete user
    }
}
