package com.wafflestudio.waflog.domain.tag.repository

import com.wafflestudio.waflog.domain.post.model.Post
import com.wafflestudio.waflog.domain.tag.model.PostTag
import com.wafflestudio.waflog.domain.tag.model.Tag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional

interface PostTagRepository : JpaRepository<PostTag, Long?> {
    fun findByPostAndTag(post: Post, tag: Tag): PostTag?

    @Transactional
    @Modifying
    @Query("DELETE FROM PostTag pt WHERE pt.id = :id")
    fun deleteMappingById(@Param("id") id: Long)

    @Transactional
    @Modifying
    @Query("DELETE FROM PostTag pt WHERE pt.post.id = :postId")
    fun deleteMappingByPostId(@Param("postId") postId: Long)
}
