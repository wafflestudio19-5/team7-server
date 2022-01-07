package com.wafflestudio.waflog.global.oauth2.service

import com.wafflestudio.waflog.domain.user.dto.UserDto
import com.wafflestudio.waflog.domain.user.repository.UserRepository
import com.wafflestudio.waflog.global.auth.JwtTokenProvider
import com.wafflestudio.waflog.global.auth.exception.TokenNotFoundException
import com.wafflestudio.waflog.global.oauth2.repository.OAuth2UserTokenRepository
import org.springframework.stereotype.Service

@Service
class OAuth2VerificationService(
    private val oAuth2UserTokenRepository: OAuth2UserTokenRepository,
    private val userRepository: UserRepository,
    private val tokenProvider: JwtTokenProvider
) {
    fun verifyAccount(token: String): Pair<UserDto.SimpleResponse, String> {
        val verificationToken = oAuth2UserTokenRepository.findByToken(token)
            ?: throw TokenNotFoundException("잘못된 소셜로그인 토큰")
        val user = userRepository.findByEmail(verificationToken.email)
            ?: throw TokenNotFoundException("잘못된 소셜로그인 토큰")
        val jwtToken = tokenProvider.generateToken(verificationToken.email)
        return Pair(UserDto.SimpleResponse(user), jwtToken)
    }
}
