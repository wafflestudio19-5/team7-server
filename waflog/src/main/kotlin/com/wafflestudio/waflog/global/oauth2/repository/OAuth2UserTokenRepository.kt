package com.wafflestudio.waflog.global.oauth2.repository

import com.wafflestudio.waflog.global.oauth2.model.OAuth2VerificationToken
import org.springframework.data.jpa.repository.JpaRepository

interface OAuth2UserTokenRepository : JpaRepository<OAuth2VerificationToken, Long?> {
    fun findByToken(token: String?): OAuth2VerificationToken?
}
