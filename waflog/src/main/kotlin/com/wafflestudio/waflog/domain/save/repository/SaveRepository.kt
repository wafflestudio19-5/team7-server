package com.wafflestudio.waflog.domain.save.repository

import com.wafflestudio.waflog.domain.save.model.Save
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional

interface SaveRepository : JpaRepository<Save, Long?> {

    @Transactional
    @Modifying
    @Query(
        "DELETE FROM Save s WHERE (s.user.id = :user_id)"
    )
    fun deleteAllUserSaves(@Param("user_id") id: Long)
}
