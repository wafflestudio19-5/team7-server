package com.wafflestudio.waflog.domain.user.repository

import com.wafflestudio.waflog.domain.user.model.Likes
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional

interface LikesRepository : JpaRepository<Likes, Long?> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Likes l WHERE l.likedPost.id = :postId")
    fun deleteMappingByPostId(@Param("postId") postId: Long)

    @Transactional
    @Modifying
    @Query("DELETE FROM Likes l WHERE l.user.id = :userId")
    fun deleteAllByUserId(@Param("userId") userId: Long)

    @Transactional
    @Modifying
    @Query("DELETE FROM Likes l WHERE l.likedPost.user.id = :userId")
    fun deleteMappingByUserId(@Param("userId") userId: Long)
}
