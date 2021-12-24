package com.wafflestudio.toy.domain.post.repository

import com.wafflestudio.toy.domain.post.model.Post
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository : JpaRepository<Post, Long?> {
    fun findAllByPrivateIsFalse(pageable: Pageable): List<Post>
}
