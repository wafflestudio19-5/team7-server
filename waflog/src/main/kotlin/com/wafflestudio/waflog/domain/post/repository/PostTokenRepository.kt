package com.wafflestudio.waflog.domain.post.repository

import com.wafflestudio.waflog.domain.post.model.PostToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.*

interface PostTokenRepository : JpaRepository<PostToken, Long?> {
    fun findByPost_Id(id: Long): PostToken?
    fun findByToken(token: String): PostToken?

    @Transactional
    @Modifying
    @Query(
        "DELETE FROM SaveToken pt WHERE (pt.post.user.id = :userId)"
    )
    fun deleteAllMappingByUserId(@Param("userId") userId: Long)
}
