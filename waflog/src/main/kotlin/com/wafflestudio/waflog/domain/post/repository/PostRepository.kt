package com.wafflestudio.waflog.domain.post.repository

import com.wafflestudio.waflog.domain.post.model.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

interface PostRepository : JpaRepository<Post, Long?> {

    @Query("SELECT p FROM Post p WHERE (p.private = false) OR p.user.userId = :userId")
    fun findRecentPosts(pageable: Pageable, @Param("userId") userId: String): Page<Post>
    fun findAllByPrivateIsFalse(pageable: Pageable): Page<Post>
    fun findAllByPrivateIsFalseAndCreatedAtAfter(pageable: Pageable, start: LocalDateTime): Page<Post>

    @Query(
        "SELECT p FROM Post p WHERE (p.private = false) AND " +
            "(p.title LIKE %:title% OR p.content LIKE %:content% OR p.user.userId = :userId)"
    )
    fun searchByKeyword(
        pageable: Pageable,
        @Param("title") title: String,
        @Param("content") content: String,
        @Param("userId") userId: String
    ): Page<Post>

    fun findByPrivateIsFalseAndUser_UserIdAndUrl(userId: String, url: String): Post?

    fun findByUser_UserIdAndUrl(userId: String, url: String): Post?

    @Query(
        "SELECT p FROM PostTag pt left join Post p ON pt.post.id = p.id " +
            "WHERE (pt.tag.id = :tagId) AND ( (p.private = false) OR (p.user.userId = :userId) ) " +
            "ORDER BY p.createdAt DESC"
    )
    fun searchByTagOnLoggedIn(
        pageable: Pageable,
        @Param("tagId") tagId: Long,
        @Param("userId") userId: String
    ): Page<Post>

    @Query(
        "SELECT p FROM PostTag pt left join Post p ON pt.post.id = p.id " +
            "WHERE (pt.tag.id = :tagId) AND (p.private = false)"
    )
    fun searchByTagOnLoggedOut(
        pageable: Pageable,
        @Param("tagId") tagId: Long
    ): Page<Post>

    @Query(
        "SELECT p FROM PostTag pt left join Post p ON pt.post.id = p.id " +
            "WHERE (pt.tag.id = :tagId) AND (p.user.userId = :userId) " +
            "ORDER BY p.createdAt DESC"
    )
    fun searchByMyTag(
        pageable: Pageable,
        @Param("tagId") tagId: Long,
        @Param("userId") userId: String
    ): Page<Post>

    @Query(
        "SELECT p FROM PostTag pt left join Post p ON pt.post.id = p.id " +
            "WHERE (pt.tag.id = :tagId) AND " +
            "(p.user.userId = :userId) AND " +
            "(p.private = false) " +
            "ORDER BY p.createdAt DESC"
    )
    fun searchByUserTag(
        pageable: Pageable,
        @Param("tagId") tagId: Long,
        @Param("userId") userId: String
    ): Page<Post>

    @Transactional
    @Modifying
    @Query("UPDATE Post p SET p.views = p.views + 1 WHERE p.id = :postId")
    fun increaseViews(@Param("postId") postId: Long)

    @Transactional
    @Modifying
    @Query(
        "DELETE FROM Post p WHERE (p.user.id = :user_id)"
    )
    fun deleteAllUserPosts(@Param("user_id") userId: Long)
}
