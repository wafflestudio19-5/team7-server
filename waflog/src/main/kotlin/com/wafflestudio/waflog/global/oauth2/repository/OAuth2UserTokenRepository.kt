package com.wafflestudio.waflog.global.oauth2.repository

import com.wafflestudio.waflog.global.auth.model.VerificationToken
import org.springframework.data.jpa.repository.JpaRepository

interface OAuth2UserTokenRepository : JpaRepository<VerificationToken, Long?> {
    fun findByToken(token: String?): VerificationToken?
    fun findByEmail(email: String?): VerificationToken?
}
