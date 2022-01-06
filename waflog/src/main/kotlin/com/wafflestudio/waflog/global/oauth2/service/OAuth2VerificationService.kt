package com.wafflestudio.waflog.global.oauth2.service

import com.wafflestudio.waflog.global.auth.exception.TokenNotFoundException
import com.wafflestudio.waflog.global.oauth2.model.OAuth2VerificationToken
import com.wafflestudio.waflog.global.oauth2.repository.OAuth2UserTokenRepository
import org.springframework.stereotype.Service

@Service
class OAuth2VerificationService(
    private val oAuth2UserTokenRepository: OAuth2UserTokenRepository
) {
    fun verifyAccount(token: String): OAuth2VerificationToken {
        return oAuth2UserTokenRepository.findByToken(token)
            ?: throw TokenNotFoundException("잘못된 소셜로그인 토큰")
    }

    fun deleteToken(oAuth2VerificationToken: OAuth2VerificationToken) {
        oAuth2UserTokenRepository.deleteById(oAuth2VerificationToken.id)
    }
}
