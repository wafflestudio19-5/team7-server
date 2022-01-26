package com.wafflestudio.waflog.domain.user.repository

import com.wafflestudio.waflog.domain.user.model.Series
import com.wafflestudio.waflog.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface SeriesRepository : JpaRepository<Series, Long?> {

    @Query("SELECT s FROM Series s WHERE s.name = :name AND s.user = :user")
    fun findByNameAndUser(@Param("name") name: String, @Param("user") user: User): Series?
}
