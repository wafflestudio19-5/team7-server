package com.wafflestudio.waflog.domain.tag.repository

import com.wafflestudio.waflog.domain.post.model.Post
import com.wafflestudio.waflog.domain.tag.dto.UserTagDto
import com.wafflestudio.waflog.domain.tag.model.PostTag
import com.wafflestudio.waflog.domain.tag.model.Tag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional

interface PostTagRepository : JpaRepository<PostTag, Long?> {
    fun findByPostAndTag(post: Post, tag: Tag): PostTag?

    @Query(
        "SELECT " +
            "new com.wafflestudio.waflog.domain.tag.dto.UserTagDto(pt.tag, COUNT(pt)) " +
            "FROM PostTag pt " +
            "WHERE pt.post.user.userId = :userId " +
            "GROUP BY pt.tag " +
            "ORDER BY COUNT(pt) DESC"
    )
    fun getMyTag(@Param("userId") userId: String): List<UserTagDto>

    @Query(
        "SELECT " +
            "new com.wafflestudio.waflog.domain.tag.dto.UserTagDto(pt.tag, COUNT(pt)) " +
            "FROM PostTag pt " +
            "WHERE (pt.post.user.userId = :userId) AND (pt.post.private = false) " +
            "GROUP BY pt.tag " +
            "ORDER BY COUNT(pt) DESC"
    )
    fun getUserTag(@Param("userId") userId: String): List<UserTagDto>

    @Transactional
    @Modifying
    @Query("DELETE FROM PostTag pt WHERE pt.id = :id")
    fun deleteMappingById(@Param("id") id: Long)

    @Transactional
    @Modifying
    @Query("DELETE FROM PostTag pt WHERE pt.post.id = :postId")
    fun deleteMappingByPostId(@Param("postId") postId: Long)

    @Transactional
    @Modifying
    @Query("DELETE FROM PostTag pt WHERE pt.post.user.id = :userId")
    fun deleteMappingByUserId(@Param("userId") userId: Long)
}
