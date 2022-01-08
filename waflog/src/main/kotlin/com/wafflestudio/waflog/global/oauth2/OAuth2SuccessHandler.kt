package com.wafflestudio.waflog.global.oauth2

import com.fasterxml.jackson.databind.ObjectMapper
import com.wafflestudio.waflog.domain.user.model.User
import com.wafflestudio.waflog.domain.user.repository.UserRepository
import com.wafflestudio.waflog.global.auth.JwtTokenProvider
import com.wafflestudio.waflog.global.auth.model.VerificationToken
import com.wafflestudio.waflog.global.oauth2.repository.OAuth2UserTokenRepository
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import java.util.UUID
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class OAuth2SuccessHandler(
    private val userRepository: UserRepository,
    private val oAuth2UserTokenRepository: OAuth2UserTokenRepository,
    private val objectMapper: ObjectMapper,
    private val jwtTokenProvider: JwtTokenProvider
) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?
    ) {
        val oAuth2User = authentication?.principal as OAuth2User
        val email = oAuth2User.attributes["email"] as String

        if (request == null || response == null)
            throw ServletException()

        // register user if not exists
        val existingUser = userRepository.findByEmail(email)

        val user = existingUser ?: run {
            userRepository.save(
                User(
                    email = email,
                    userId = (oAuth2User.attributes["userId"] as String),
                    name = (oAuth2User.attributes["name"] as String),
                    image = oAuth2User.attributes["image"]?.let(Any::toString)
                        ?: "https://wafflestudio.com/_next/image?url=%2Fimages%2Ficon_intro.svg&w=640&q=75",
                    pageTitle = (oAuth2User.attributes["pageTitle"] as String)
                )
            )
        }

        // generate verification token for JWT authentication
        val token = generateOAuth2UserToken(email)

        // write JWT token to response
        val jwt = jwtTokenProvider.generateToken(email)
        response.addHeader("Location", "https://waflog-web.kro.kr/social?token=$jwt")
        response.status = HttpServletResponse.SC_TEMPORARY_REDIRECT
    }

    private fun generateOAuth2UserToken(email: String): String {
        val token = UUID.randomUUID().toString()

        val existingToken = oAuth2UserTokenRepository.findByEmail(email)

        existingToken?.also { // reuse token if exists: unique OAuth2UserToken
            it.token = token
            oAuth2UserTokenRepository.save(it)
        } ?: run {
            val verificationToken = VerificationToken(email, token)
            oAuth2UserTokenRepository.save(verificationToken)
        }

        return token
    }
}
