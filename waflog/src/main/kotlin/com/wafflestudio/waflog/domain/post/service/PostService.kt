package com.wafflestudio.waflog.domain.post.service

import com.wafflestudio.waflog.domain.image.dto.ImageDto
import com.wafflestudio.waflog.domain.image.model.Image
import com.wafflestudio.waflog.domain.image.repository.ImageRepository
import com.wafflestudio.waflog.domain.image.service.ImageService
import com.wafflestudio.waflog.domain.post.dto.CommentDto
import com.wafflestudio.waflog.domain.post.dto.PostDto
import com.wafflestudio.waflog.domain.post.exception.CommentNotFoundException
import com.wafflestudio.waflog.domain.post.exception.CommentNotWrittenByUserException
import com.wafflestudio.waflog.domain.post.exception.InvalidPostTitleException
import com.wafflestudio.waflog.domain.post.exception.PostNotFoundException
import com.wafflestudio.waflog.domain.post.model.Comment
import com.wafflestudio.waflog.domain.post.model.Post
import com.wafflestudio.waflog.domain.post.model.PostToken
import com.wafflestudio.waflog.domain.post.repository.CommentRepository
import com.wafflestudio.waflog.domain.post.repository.PostRepository
import com.wafflestudio.waflog.domain.post.repository.PostTokenRepository
import com.wafflestudio.waflog.domain.tag.model.PostTag
import com.wafflestudio.waflog.domain.tag.model.Tag
import com.wafflestudio.waflog.domain.tag.repository.PostTagRepository
import com.wafflestudio.waflog.domain.tag.repository.TagRepository
import com.wafflestudio.waflog.domain.user.exception.SeriesNotFoundException
import com.wafflestudio.waflog.domain.user.model.Likes
import com.wafflestudio.waflog.domain.user.model.Reads
import com.wafflestudio.waflog.domain.user.model.User
import com.wafflestudio.waflog.domain.user.repository.LikesRepository
import com.wafflestudio.waflog.domain.user.repository.ReadsRepository
import com.wafflestudio.waflog.domain.user.repository.SeriesRepository
import com.wafflestudio.waflog.global.common.dto.ListResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.UUID

@Service
class PostService(
    private val postRepository: PostRepository,
    private val postTokenRepository: PostTokenRepository,
    private val seriesRepository: SeriesRepository,
    private val commentRepository: CommentRepository,
    private val likesRepository: LikesRepository,
    private val readsRepository: ReadsRepository,
    private val imageRepository: ImageRepository,
    private val imageService: ImageService,
    private val tagRepository: TagRepository,
    private val postTagRepository: PostTagRepository
) {
    fun getRecentPosts(pageable: Pageable, user: User?): Page<PostDto.MainPageResponse> {
        val posts: Page<Post> =
            user?.let { postRepository.findRecentPosts(pageable, it.userId) }
                ?: postRepository.findAllByPrivateIsFalse(pageable)
        return posts.map { post -> PostDto.MainPageResponse(post) }
    }

    fun getTrendingPosts(pageable: Pageable, date: Int): Page<PostDto.MainPageResponse> {
        val dateStart = LocalDate.now().atStartOfDay().minusDays(date.toLong())
        val posts = postRepository.findAllByPrivateIsFalseAndCreatedAtAfter(pageable, dateStart)
        return posts.map { post -> PostDto.MainPageResponse(post) }
    }

    fun searchPosts(pageable: Pageable, keyword: String): Page<PostDto.MainPageResponse> {
        return if (keyword != "") {
            val posts = postRepository.searchByKeyword(pageable, keyword, keyword, keyword)
            posts.map { post -> PostDto.MainPageResponse(post) }
        } else {
            Page.empty()
        }
    }

    fun getPostDetail(id: Long, user: User?): PostDto.PageDetailResponse {
        val post = postRepository.findByIdOrNull(id)
            ?: throw PostNotFoundException("There is no post id $id")
        applyUserReadPost(user, post)
        return PostDto.PageDetailResponse(post, user)
    }

    fun getPostDetailWithURL(userId: String, postURL: String, user: User?): PostDto.PageDetailResponse {
        val post = postRepository.findByPrivateIsFalseAndUser_UserIdAndUrl(userId, postURL)
            ?: throw PostNotFoundException("There is no post with url '@$userId/$postURL'")
        applyUserReadPost(user, post)
        return PostDto.PageDetailResponse(post, user)
    }

    private fun applyUserReadPost(user: User?, post: Post) {
        user?.also {
            postRepository.increaseViews(post.id)
            readsRepository.findByUser_UserIdAndReadPost_Id(user.userId, post.id)
                ?: run { readsRepository.save(Reads(user, post)) }
        }
    }

    fun writePost(createRequest: PostDto.CreateRequest, user: User) {
        val title = createRequest.title
        if (title.isBlank()) throw InvalidPostTitleException("제목이 비어있습니다.")
        val content = createRequest.content
        val images = formatImageList(createRequest.images, user)
        val thumbnail = createRequest.thumbnail
        val summary = createRequest.summary
        val private = createRequest.private
        var url = formatUrl(createRequest.url, title)
        postRepository.findByUser_UserIdAndUrl(user.userId, url)
            ?.also { url += "-" + getRandomString(8) }
        val seriesName = createRequest.seriesName
        val series = seriesName?.let {
            seriesRepository.findByName(it) ?: throw SeriesNotFoundException("series not found")
        }
        val seriesOrder = series?.let { series.posts.size + 1 }
        val tags = createRequest.tags.map { formatTagNameUrl(it) }.map { (name, url) ->
            tagRepository.findByUrl(url)
                ?: tagRepository.save(Tag(name, url))
        }

        val post = Post(
            user = user,
            title = title,
            content = content,
            thumbnail = thumbnail,
            summary = summary,
            private = private,
            url = url,
            series = series,
            seriesOrder = seriesOrder,
            images = mutableListOf(),
            comments = mutableListOf(),
            likedUser = mutableListOf()
        )
        postRepository.save(post)
            .also {
                images.map { image ->
                    image.post = it
                    imageRepository.save(image)
                }
            }

        tags.forEach { tag ->
            postTagRepository.save(PostTag(post, tag))
        }
    }

    @Transactional
    fun modifyPost(putRequest: PostDto.PutRequest, user: User) {
        val token = putRequest.token
        val title = putRequest.title
        if (title.isBlank()) throw InvalidPostTitleException("제목이 비어있습니다.")
        var url = formatUrl(putRequest.url, title)
        val seriesName = putRequest.seriesName
        val series = seriesName?.let {
            seriesRepository.findByName(it) ?: throw SeriesNotFoundException("series not found")
        }
        var seriesOrder = series?.let { series.posts.size + 1 }

        val newTagNames = putRequest.tags.toSet()

        postTokenRepository.findByToken(token)?.post
            ?.also {
                if (it.url != url) {
                    postRepository.findByUser_UserIdAndUrl(user.userId, url)
                        ?.let { url += "-" + getRandomString(8) }
                }
                if (it.series == series) {
                    seriesOrder = it.seriesOrder
                }
            }
            ?.apply {
                this.title = title
                this.content = putRequest.content
                this.thumbnail = putRequest.thumbnail
                this.summary = putRequest.summary
                this.private = putRequest.private
                this.url = url
                this.series = series
                this.seriesOrder = seriesOrder
            }
            ?.also { post ->
                postRepository.save(post)

                // delete tag not in new tag list
                post.postTags.filter { pt ->
                    !newTagNames.contains(pt.tag.name)
                }.forEach { pt ->
                    postTagRepository.deleteMappingById(pt.id)
                }

                val oldTagNames = post.postTags.map { pt -> pt.tag.name }.toSet()

                // create mapping for tag not in old tag list
                (newTagNames subtract oldTagNames).map { name -> formatTagNameUrl(name) }
                    .map { (name, url) ->
                        tagRepository.findByUrl(url)
                            ?: tagRepository.save(Tag(name, url))
                    }.forEach { tag ->
                        postTagRepository.save(PostTag(post, tag))
                    }

                tagRepository.deleteUnusedTags()
            }
            ?.also {
                modifyImageList(putRequest.images, it, user)
                    .map { image ->
                        image.post = it
                        imageRepository.save(image)
                    }
                    ?: throw PostNotFoundException("There is no post with token <$token>")
            }
    }

    fun generatePostToken(url: String, user: User): String {
        val post = postRepository.findByUser_UserIdAndUrl(user.userId, url)!!
        val token = UUID.randomUUID().toString()

        postTokenRepository.findByPost_Id(post.id)
            ?.apply { this.token = token }
            ?.also { postTokenRepository.save(it) }
            ?: run { postTokenRepository.save(PostToken(post, token)) }
        return token
    }

    @Transactional
    fun deletePost(url: String, user: User) {
        postRepository.findByUser_UserIdAndUrl(user.userId, url)
            ?.let { post ->
                postTokenRepository.findByPost_Id(post.id)
                    ?.let { postTokenRepository.deleteById(it.id) }
                likesRepository.deleteMappingByPostId(post.id)
                readsRepository.deleteMappingByPostId(post.id)
                commentRepository.deleteCommentsByPostId(post.id)
                postTagRepository.deleteMappingByPostId(post.id)
                postRepository.deleteById(post.id)
                deletePostImage(post.id, user)
                postRepository.deleteById(post.id)
                tagRepository.deleteUnusedTags()
            }
            ?: throw PostNotFoundException("post not found with url '$url'")
    }

    fun writeComment(
        postId: Long,
        createRequest: CommentDto.CreateRequest,
        user: User
    ): ListResponse<CommentDto.RootCommentResponse> {
        val post = postRepository.findByIdOrNull(postId)
            ?: throw PostNotFoundException("Post with id $postId does not exist")

        createRequest.parentComment?.also { // if the comment is a reply
            val parentId = createRequest.parentComment

            val parentComment = commentRepository.findByIdOrNull(parentId)
                ?: throw CommentNotFoundException("Parent comment with id $parentId does not exist in the post")
            if (parentComment.post.id != post.id)
                throw CommentNotFoundException("Parent comment with id $parentId does not exist in the post")

            val rootComment = parentComment.rootComment

            // Nested set adjustment
            val right = parentComment.rgt
            commentRepository.updateLeft(rootComment, right, 2)
            commentRepository.updateRight(rootComment, right, 2)

            val comment = Comment(
                user = user,
                post = post,
                content = createRequest.content,
                rootComment = rootComment,
                lft = right,
                rgt = right + 1,
                depth = parentComment.depth + 1
            )

            commentRepository.save(comment)
        } ?: run { // if the comment is a root
            val comment = commentRepository.save(
                Comment(
                    user = user,
                    post = post,
                    content = createRequest.content
                )
            )

            comment.rootComment = comment.id // set root comment of the comment to itself
            commentRepository.save(comment)
        }

        return PostDto.getCommentListResponse(post.comments)
    }

    fun modifyComment(
        postId: Long,
        commentId: Long,
        modifyRequest: CommentDto.ModifyRequest,
        user: User
    ): ListResponse<CommentDto.RootCommentResponse> {

        val post = postRepository.findByIdOrNull(postId)
            ?: throw PostNotFoundException("Post with id $postId does not exist")

        val comment = commentRepository.findByIdOrNull(commentId)
            ?: throw CommentNotFoundException("Comment with id $commentId does not exist in the post")

        if (comment.post.id != post.id)
            throw CommentNotFoundException("Comment with id $commentId does not exist in the post")

        if (comment.user?.id != user.id)
            throw CommentNotWrittenByUserException("You did not write this comment")

        comment.content = modifyRequest.content

        commentRepository.save(comment)

        return PostDto.getCommentListResponse(post.comments)
    }

    fun deleteComment(
        postId: Long,
        commentId: Long,
        user: User
    ): ListResponse<CommentDto.RootCommentResponse> {

        val post = postRepository.findByIdOrNull(postId)
            ?: throw PostNotFoundException("Post with id $postId does not exist")

        val comment = commentRepository.findByIdOrNull(commentId)
            ?: throw CommentNotFoundException("Comment with id $commentId does not exist in the post")

        if (comment.post.id != post.id)
            throw CommentNotFoundException("Comment with id $commentId does not exist in the post")

        if (comment.user?.id != user.id)
            throw CommentNotWrittenByUserException("You did not write this comment")

        if (comment.depth == 0) { // if comment to delete is root
            if (post.comments.any { it.depth > 0 && it.rootComment == comment.id }) {
                // if comment has replies, mark as deleted
                comment.user = null
                commentRepository.save(comment)
            } else {
                commentRepository.deleteById(comment.id)
            }
        } else { //  if comment to delete is reply
            val rootComment = comment.rootComment
            val left = comment.lft
            val right = comment.rgt
            val width = right - left + 1

            // delete all replies under the comment
            commentRepository.deleteReplies(rootComment, left, right)

            // nested set adjustment
            commentRepository.updateLeft(rootComment, right, -width)
            commentRepository.updateRight(rootComment, right, -width)
        }

        return PostDto.getCommentListResponse(post.comments)
    }

    fun clickLikeInPost(postId: Long, user: User): PostDto.PostLikesResponse {
        val post: Post = postRepository.findByIdOrNull(postId)
            ?: throw PostNotFoundException("Post with id $postId does not exist")
        val likes = user.likedPosts.find { likes -> likes.likedPost.id == post.id }
        if (likes == null) {
            likesRepository.save(Likes(user, post))
        } else {
            likesRepository.deleteById(likes.id)
        }
        return PostDto.PostLikesResponse(post, likes == null)
    }

    fun isLikedPost(postId: Long, user: User): Boolean {
        val post: Post = postRepository.findByIdOrNull(postId)
            ?: throw PostNotFoundException("Post with id $postId does not exist")
        val likes = user.likedPosts.find { likes -> likes.likedPost.id == post.id }
        return likes != null
    }

    private fun getRandomString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    private fun formatUrl(url: String, title: String): String {
        url.replace(" ", "-")
            .let {
                return it.ifBlank {
                    return if (it == "") {
                        return if (title.length == 1) title + "-" + getRandomString(8)
                        else title.replace(" ", "-")
                    } else getRandomString(8)
                }
            }
    }

    private fun deletePostImage(postId: Long, user: User) {
        imageRepository.findAllByUser_UserIdAndPost_Id(user.userId, postId)
            .map { imageService.removeImage(ImageDto.RemoveRequest(it.token), user) }
    }

    private fun formatImageList(images: List<ImageDto.S3Token>, user: User): List<Image> {
        val removeImageNotExistInContent = { token: String ->
            imageService.removeImage(ImageDto.RemoveRequest(token), user)
            null
        }
        return images
            .mapNotNull {
                imageRepository.findByUser_UserIdAndToken(user.userId, it.token)
                    ?: removeImageNotExistInContent(it.token)
            }
    }

    private fun modifyImageList(images: List<ImageDto.S3Token>, post: Post, user: User): List<Image> {
        post.images.map {
            if (!images.contains(ImageDto.S3Token(it.token)))
                imageService.removeImage(ImageDto.RemoveRequest(it.token), user)
        }
        return formatImageList(images, user)
    }

    private fun formatTagNameUrl(name: String): Pair<String, String> {
        val formattedName = if (name.contains("-")) """"$name"""" else name

        val blacklist = """[?!]""".toRegex()
        val url = name.replace(" ", "-").replace(blacklist, "")

        return Pair(formattedName, url)
    }
}
