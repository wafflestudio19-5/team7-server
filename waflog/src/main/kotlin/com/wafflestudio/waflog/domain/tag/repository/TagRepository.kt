package com.wafflestudio.waflog.domain.tag.repository

import com.wafflestudio.waflog.domain.tag.model.Tag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional

interface TagRepository : JpaRepository<Tag, Long?> {
    fun findByUrl(url: String): Tag?

    @Transactional
    @Modifying
    @Query("DELETE FROM Post p WHERE (SELECT count(pt) FROM PostToken pt WHERE pt.post.id = p.id) = 0")
    fun deleteUnusedTags()
}
