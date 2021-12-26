package com.wafflestudio.toy.domain.user.repository

import com.wafflestudio.toy.domain.user.model.Series
import org.springframework.data.jpa.repository.JpaRepository

interface SeriesRepository:JpaRepository<Series, Long?> {
}