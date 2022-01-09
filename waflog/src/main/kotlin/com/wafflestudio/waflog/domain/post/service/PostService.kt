package com.wafflestudio.waflog.domain.post.service

import com.wafflestudio.waflog.domain.post.dto.CommentDto
import com.wafflestudio.waflog.domain.post.dto.PostDto
import com.wafflestudio.waflog.domain.post.exception.CommentNotFoundException
import com.wafflestudio.waflog.domain.post.exception.CommentNotWrittenByUserException
import com.wafflestudio.waflog.domain.post.exception.InvalidPostFormException
import com.wafflestudio.waflog.domain.post.exception.PostNotFoundException
import com.wafflestudio.waflog.domain.post.model.Comment
import com.wafflestudio.waflog.domain.post.model.Post
import com.wafflestudio.waflog.domain.post.repository.CommentRepository
import com.wafflestudio.waflog.domain.post.repository.PostRepository
import com.wafflestudio.waflog.domain.user.exception.SeriesNotFoundException
import com.wafflestudio.waflog.domain.user.model.Likes
import com.wafflestudio.waflog.domain.user.model.User
import com.wafflestudio.waflog.domain.user.repository.LikesRepository
import com.wafflestudio.waflog.domain.user.repository.SeriesRepository
import com.wafflestudio.waflog.global.common.dto.ListResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class PostService(
    private val postRepository: PostRepository,
    private val seriesRepository: SeriesRepository,
    private val commentRepository: CommentRepository,
    private val likesRepository: LikesRepository
) {
    fun getRecentPosts(pageable: Pageable): Page<PostDto.MainPageResponse> {
        val posts: Page<Post> =
            postRepository.findAllByPrivateIsFalse(pageable)
        return posts.map { post -> PostDto.MainPageResponse(post) }
    }

    fun getTrendingPosts(pageable: Pageable, date: Int): Page<PostDto.MainPageResponse> {
        val dateStart = LocalDate.now().atStartOfDay().minusDays(date.toLong())
        val posts = postRepository.findAllByPrivateIsFalseAndCreatedAtAfter(pageable, dateStart)
        return posts.map { post -> PostDto.MainPageResponse(post) }
    }

    fun getPostDetail(id: Long): PostDto.PageDetailResponse {
        val post = postRepository.findByIdOrNull(id)
        post?.let { p -> return PostDto.PageDetailResponse(p) }
        throw PostNotFoundException("There is no post id $id")
    }

    fun searchPosts(pageable: Pageable, keyword: String): Page<PostDto.MainPageResponse> {
        val posts = postRepository.searchByKeyword(pageable, keyword, keyword, keyword)
        return posts.map { post -> PostDto.MainPageResponse(post) }
    }

    fun getPostDetailWithURL(userId: String, postURL: String): PostDto.PageDetailResponse {
        val post = postRepository.findByPrivateIsFalseAndUser_UserIdAndUrl(userId, postURL)
            ?: throw PostNotFoundException("There is no post with url '@$userId/$postURL'")
        return PostDto.PageDetailResponse(post)
    }

    private fun getRandomString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    fun writePost(createRequest: PostDto.CreateRequest, user: User) {
        val title = createRequest.title
        if (title == "") throw InvalidPostFormException("제목이 비어있습니다.")
        val content = createRequest.content
        val thumbnail = createRequest.thumbnail
        val summary = createRequest.summary
        val private = createRequest.private
        var url = createRequest.url.replace(" ", "-")
        if (url == "") {
            url = if (title.length == 1) title + "-" + getRandomString(8)
            else title.replace(" ", "-")
        }
        val seriesName = createRequest.seriesName
        val series = seriesName?.let {
            seriesRepository.findByName(it) ?: throw SeriesNotFoundException("series not found")
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
            comments = mutableListOf(),
            postTags = mutableListOf(),
            likedUser = mutableListOf()

        )

        postRepository.save(post)
    }

    fun writeComment(
        postId: Long,
        createRequest: CommentDto.CreateRequest,
        user: User
    ): ListResponse<CommentDto.RootCommentResponse> {
        val post = postRepository.findByIdOrNull(postId)
            ?: throw PostNotFoundException("Post with id $postId does not exist")

        if (createRequest.parentComment == 0L) {
            val comment = Comment(
                user = user,
                post = post,
                content = createRequest.content
            )
            commentRepository.save(comment)
        } else {
            val parentId = createRequest.parentComment

            val parentComment = commentRepository.findByIdOrNull(parentId)
                ?: throw CommentNotFoundException("Parent comment with id $parentId does not exist in the post")
            if (parentComment.post.id != post.id)
                throw CommentNotFoundException("Parent comment with id $parentId does not exist in the post")

            val rootComment = if (parentComment.depth == 0) parentComment.id else parentComment.rootComment

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
        }

        return ListResponse(
            post.comments.size,
            post.comments
                .filter { it.depth == 0 }
                .map { root ->
                    CommentDto.RootCommentResponse(
                        root,
                        post.comments.filter { it.rootComment == root.id }
                    )
                }
        )
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

        return ListResponse(
            post.comments.size,
            post.comments
                .filter { it.depth == 0 }
                .map { root ->
                    CommentDto.RootCommentResponse(
                        root,
                        post.comments.filter { it.rootComment == root.id }
                    )
                }
        )
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
            if (post.comments.any { it.rootComment == comment.id }) { // if comment has replies, mark as deleted
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

        return ListResponse(
            post.comments.size,
            post.comments
                .filter { it.depth == 0 }
                .map { root ->
                    CommentDto.RootCommentResponse(
                        root,
                        post.comments.filter { it.rootComment == root.id }
                    )
                }
        )
    }

    fun addLikeInPost(postId: Long, user: User): PostDto.PageDetailResponse {
        val post: Post = postRepository.findByIdOrNull(postId)
            ?: throw PostNotFoundException("Post with id $postId does not exist")
        val likes = user.likedPosts.find { likes -> likes.likedPost.id == post.id }
        if (likes == null) {
            likesRepository.save(Likes(user, post))
        } else {
            likesRepository.deleteById(likes.id)
        }
        return PostDto.PageDetailResponse(post)
    }

    fun isLikedPost(postId: Long, user: User): Boolean {
        val post: Post = postRepository.findByIdOrNull(postId)
            ?: throw PostNotFoundException("Post with id $postId does not exist")
        val likes = user.likedPosts.find { likes -> likes.likedPost.id == post.id }
        return likes != null
    }
}
