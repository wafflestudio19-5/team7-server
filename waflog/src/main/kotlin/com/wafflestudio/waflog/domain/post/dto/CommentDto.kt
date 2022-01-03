package com.wafflestudio.waflog.domain.post.dto

import com.wafflestudio.waflog.domain.post.model.Comment
import com.wafflestudio.waflog.domain.user.dto.UserDto
import java.time.LocalDateTime

class CommentDto {
    data class CommentResponse(
        val id: Long,
        val user: UserDto.SimpleResponse,
        val content: String,
        val depth: Int,
        val rootComment: Long,
        val createdAt: LocalDateTime?
    ) {
        constructor(comment: Comment) : this(
            id = comment.id,
            user = comment.user?.let { UserDto.SimpleResponse(it) }
                ?: UserDto.SimpleResponse(id = -1, userId = "알 수 없음", image = ""),
            content = comment.user?.let { comment.content } ?: "삭제된 댓글입니다.",
            depth = comment.depth,
            rootComment = comment.rootComment,
            createdAt = comment.createdAt
        )
    }

    data class RootCommentResponse(
        val rootComment: CommentResponse,
        val replyNumber: Int,
        val replies: List<CommentResponse>
    ) {
        constructor(rootComment: Comment, replyList: List<Comment>) : this(
            rootComment = CommentResponse(rootComment),
            replyNumber = replyList.size,
            replies = replyList.map { CommentResponse(it) }
        )
    }
}
