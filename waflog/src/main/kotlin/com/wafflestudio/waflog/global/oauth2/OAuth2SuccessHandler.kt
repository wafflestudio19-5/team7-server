package com.wafflestudio.waflog.global.oauth2

import com.fasterxml.jackson.databind.ObjectMapper
import com.wafflestudio.waflog.domain.user.dto.UserDto
import com.wafflestudio.waflog.domain.user.model.User
import com.wafflestudio.waflog.domain.user.repository.UserRepository
import com.wafflestudio.waflog.global.auth.JwtTokenProvider
import com.wafflestudio.waflog.global.auth.dto.VerificationTokenPrincipalDto
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

        val existingUser = userRepository.findByEmail(email)

        if (request == null || response == null)
            throw ServletException()

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

        val token = generateOAuth2UserToken(email)

        val jwt = jwtTokenProvider.generateToken(email)
        response.addHeader("Authentication", jwt)
        response.status = HttpServletResponse.SC_OK
        response.contentType = "application/json"
        response.characterEncoding = "utf-8"

        val out = response.writer

        val userJsonString = objectMapper.writeValueAsString(
            VerificationTokenPrincipalDto(UserDto.SimpleResponse(user), jwt)
        )
        out.print(userJsonString)

        out.flush()
    }

    private fun generateOAuth2UserToken(email: String): String {
        val token = UUID.randomUUID().toString()

        val existingToken = oAuth2UserTokenRepository.findByEmail(email)

        existingToken?.also {
            it.token = token
            oAuth2UserTokenRepository.save(it)
        } ?: run {
            val verificationToken = VerificationToken(email, token)
            oAuth2UserTokenRepository.save(verificationToken)
        }

        return token
    }
}
