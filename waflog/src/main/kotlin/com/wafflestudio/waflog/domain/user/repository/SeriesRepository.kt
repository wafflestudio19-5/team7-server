package com.wafflestudio.waflog.domain.user.repository

import com.wafflestudio.waflog.domain.user.model.Series
import org.springframework.data.jpa.repository.JpaRepository

interface SeriesRepository:JpaRepository<Series, Long?> {
}
