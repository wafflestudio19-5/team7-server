package com.wafflestudio.waflog.global.auth.repository

import com.wafflestudio.waflog.global.auth.model.VerificationToken
import org.springframework.data.jpa.repository.JpaRepository

interface VerificationTokenRepository : JpaRepository<VerificationToken, Long?> {
    fun findByToken(token: String?): VerificationToken?
    fun findByEmail(email: String?): VerificationToken?
}
