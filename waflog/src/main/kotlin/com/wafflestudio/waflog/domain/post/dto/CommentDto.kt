package com.wafflestudio.waflog.domain.post.dto

import com.wafflestudio.waflog.domain.post.model.Comment
import com.wafflestudio.waflog.domain.user.dto.UserDto
import com.wafflestudio.waflog.global.common.dto.ListResponse
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank

class CommentDto {
    data class CommentResponse(
        val id: Long,
        val user: UserDto.SimpleResponse,
        val content: String,
        val depth: Int,
        val createdAt: LocalDateTime?
    ) {
        constructor(comment: Comment) : this(
            id = comment.id,
            user = comment.user?.let { UserDto.SimpleResponse(it) }
                ?: UserDto.SimpleResponse(id = -1, userId = "알 수 없음", image = ""),
            content = comment.user?.let { comment.content } ?: "삭제된 댓글입니다.",
            depth = comment.depth,
            createdAt = comment.createdAt
        )
    }

    data class RootCommentResponse(
        val rootComment: CommentResponse,
        val replies: ListResponse<CommentResponse>
    ) {
        constructor(rootComment: Comment, replyList: List<Comment>) : this(
            rootComment = CommentResponse(rootComment),
            replies = ListResponse(replyList.map { reply -> CommentResponse(reply) })
        )
    }

    data class CreateRequest(
        val parentComment: Long? = null,

        @field:NotBlank
        val content: String,
    )

    data class ModifyRequest(
        @field:NotBlank
        val content: String
    )
}
