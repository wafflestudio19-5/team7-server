package com.wafflestudio.waflog.domain.user.repository

import com.wafflestudio.waflog.domain.user.model.Series
import com.wafflestudio.waflog.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface SeriesRepository : JpaRepository<Series, Long?> {
    fun findByName(name: String): Series?

    @Query("SELECT s FROM Series s WHERE s.name =: name AND s.user =: user")
    fun findByNameAndUser(name: String, user: User): Series?
}
