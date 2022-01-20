package com.wafflestudio.waflog.domain.post.repository

import com.wafflestudio.waflog.domain.post.model.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional

interface CommentRepository : JpaRepository<Comment, Long?> {

    @Transactional
    @Modifying
    @Query(
        "UPDATE Comment c SET c.lft = c.lft + :move WHERE " +
            "((c.rootComment = :rootComment OR c.id = :rootComment) AND c.lft > :rgt)"
    )
    fun updateLeft(@Param("rootComment") rootComment: Long, @Param("rgt") right: Long, @Param("move") move: Long)

    @Transactional
    @Modifying
    @Query(
        "UPDATE Comment c SET c.rgt = c.rgt + :move WHERE " +
            "((c.rootComment = :rootComment OR c.id = :rootComment) AND c.rgt >= :rgt)"
    )
    fun updateRight(@Param("rootComment") rootComment: Long, @Param("rgt") right: Long, @Param("move") move: Long)

    @Transactional
    @Modifying
    @Query(
        "DELETE FROM Comment c WHERE (c.rootComment = :rootComment AND (c.lft BETWEEN :lft AND :rgt))"
    )
    fun deleteReplies(@Param("rootComment") rootComment: Long, @Param("lft") left: Long, @Param("rgt") right: Long)

    @Transactional
    @Modifying
    @Query("DELETE FROM Comment c WHERE c.post.id = :postId")
    fun deleteCommentsByPostId(@Param("postId") postId: Long)

    @Transactional
    @Modifying
    @Query("UPDATE Comment c SET c.user = null WHERE c.user.id = :userId")
    fun updateCommentWriterByNull(@Param("userId") userId: Long)

    @Transactional
    @Modifying
    @Query("DELETE FROM Comment c WHERE (c.post.user.id = :userId)")
    fun deleteAllCommentMappingByUserId(@Param("userId") userId: Long)
}
