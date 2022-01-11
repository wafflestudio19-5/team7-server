package com.wafflestudio.waflog.global.auth.repository

import com.wafflestudio.waflog.global.auth.model.SignUpAttempt
import org.springframework.data.jpa.repository.JpaRepository

interface SignUpAttemptRepository : JpaRepository<SignUpAttempt, Long?> {
    fun findByEmail(email: String): SignUpAttempt?
    fun findByJwt(jwt: String): SignUpAttempt?
}
