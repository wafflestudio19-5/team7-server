package com.wafflestudio.waflog.domain.save.repository

import com.wafflestudio.waflog.domain.save.model.SaveToken
import com.wafflestudio.waflog.domain.user.model.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional

interface SaveTokenRepository : JpaRepository<SaveToken, Long?> {

    fun findAllBySave_User_Id(pageable: Pageable, id: Long): Page<SaveToken>

    fun findByTokenAndSave_User(token: String, user: User): SaveToken?

    @Transactional
    @Modifying
    @Query(
        "DELETE FROM SaveToken st WHERE st.save.id in " +
            "(SELECT s.id FROM Save s WHERE (s.user.id = :userId))"
    )
    fun deleteAllMappingByUserId(@Param("userId") id: Long)
}
