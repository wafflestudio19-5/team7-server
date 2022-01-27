package com.wafflestudio.waflog.domain.user.repository

import com.wafflestudio.waflog.domain.user.model.Series
import com.wafflestudio.waflog.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import javax.transaction.Transactional

interface SeriesRepository : JpaRepository<Series, Long?> {

    @Query("SELECT s FROM Series s WHERE s.name = :name AND s.user = :user")
    fun findByNameAndUser(@Param("name") name: String, @Param("user") user: User): Series?

    @Transactional
    @Modifying
    @Query(
        "DELETE FROM Series s WHERE s.user.id = :userId"
    )
    fun deleteMappingByUserId(@Param("userId") userId: Long)
}
