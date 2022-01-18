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
    @Query("DELETE FROM Tag t WHERE (SELECT count(pt) FROM PostTag pt WHERE pt.tag.id = t.id) = 0")
    fun deleteUnusedTags()
}
