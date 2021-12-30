package com.wafflestudio.toy.domain.post.repository

import com.wafflestudio.toy.domain.post.model.Post
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface PostRepository : JpaRepository<Post, Long?> {
    fun findAllByPrivateIsFalse(pageable: Pageable): List<Post>
    fun findAllByPrivateIsFalseAndCreatedAtAfter(pageable: Pageable, start: LocalDateTime): List<Post>
}
