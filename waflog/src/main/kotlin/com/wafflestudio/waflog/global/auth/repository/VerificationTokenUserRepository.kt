package com.wafflestudio.waflog.global.auth.repository

import com.wafflestudio.waflog.global.auth.model.VerificationTokenUser
import org.springframework.data.jpa.repository.JpaRepository

interface VerificationTokenUserRepository : JpaRepository<VerificationTokenUser, Long?> {
    fun findByToken(token: String?): VerificationTokenUser?
    fun findByEmail(email: String?): VerificationTokenUser?
}
