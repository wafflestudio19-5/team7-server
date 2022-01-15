package com.wafflestudio.waflog.domain.post.repository

import com.wafflestudio.waflog.domain.post.model.PostToken
import org.springframework.data.jpa.repository.JpaRepository

interface PostTokenRepository : JpaRepository<PostToken, Long?> {
    fun findByPost_Id(id: Long): PostToken?
    fun findByToken(token: String): PostToken?
}
