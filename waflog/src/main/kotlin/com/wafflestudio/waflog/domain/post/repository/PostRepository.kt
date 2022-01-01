package com.wafflestudio.waflog.domain.post.repository

import com.wafflestudio.waflog.domain.post.model.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface PostRepository : JpaRepository<Post, Long?> {
    fun findAllByPrivateIsFalse(pageable: Pageable): Page<Post>
    fun findAllByPrivateIsFalseAndCreatedAtAfter(pageable: Pageable, start: LocalDateTime): Page<Post>

    @Query(
        "SELECT p FROM Post p WHERE (p.private = false) AND " +
            "(p.title LIKE %:title% OR p.content LIKE %:content% OR p.user.userName = :username)"
    )
    fun searchByKeyword(
        pageable: Pageable,
        @Param("title") title: String,
        @Param("content") content: String,
        @Param("username") username: String
    ): Page<Post>
}
