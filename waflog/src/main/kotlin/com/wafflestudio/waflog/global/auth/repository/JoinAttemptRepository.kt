package com.wafflestudio.waflog.global.auth.repository

import com.wafflestudio.waflog.global.auth.model.JoinAttempt
import org.springframework.data.jpa.repository.JpaRepository

interface JoinAttemptRepository : JpaRepository<JoinAttempt, Long?> {
    fun findByEmail(email: String): JoinAttempt?
}
