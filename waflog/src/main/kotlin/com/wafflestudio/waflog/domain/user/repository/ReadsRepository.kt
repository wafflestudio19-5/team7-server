package com.wafflestudio.waflog.domain.user.repository

import com.wafflestudio.waflog.domain.user.model.Reads
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional

interface ReadsRepository : JpaRepository<Reads, Long?> {
    fun findByUser_UserIdAndReadPost_Id(userId: String, postId: Long): Reads?

    @Transactional
    @Modifying
    @Query("DELETE FROM Reads r WHERE r.readPost.id = :postId")
    fun deleteMappingByPostId(@Param("postId") postId: Long)

    @Transactional
    @Modifying
    @Query(
        "DELETE FROM Reads r WHERE r.readPost.id in " +
            "(SELECT rp.id FROM Post rp WHERE (rp.user.id = :userId))"
    )
    fun deleteMappingByUserIdOfPost(@Param("userId") userId: Long)

    @Transactional
    @Modifying
    @Query(
        "DELETE FROM Reads r WHERE r.user.id = :userId"
    )
    fun deleteMappingByUserId(@Param("userId") userId: Long)
}
