package com.wafflestudio.waflog.global.oauth2

import com.wafflestudio.waflog.domain.user.repository.UserRepository
import com.wafflestudio.waflog.global.auth.JwtTokenProvider
import com.wafflestudio.waflog.global.auth.model.VerificationToken
import com.wafflestudio.waflog.global.auth.repository.VerificationTokenRepository
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class OAuth2SuccessHandler(
    private val userRepository: UserRepository,
    private val verificationTokenRepository: VerificationTokenRepository,
    private val jwtTokenProvider: JwtTokenProvider,
    private val passwordEncoder: PasswordEncoder
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
        userRepository.findByEmail(email)?.run {
            // generate verification token for JWT authentication
            val jwt = jwtTokenProvider.generateToken(email, join = false)

            // return redirect response
            response.addHeader("Location", "https://waflog-web.kro.kr/social?token=$jwt")
            response.status = HttpServletResponse.SC_TEMPORARY_REDIRECT
        } ?: run {
            // save image URL if exists
            val image = oAuth2User.attributes["image"]
                ?.let(Any::toString)
                ?: "https://wafflestudio.com/_next/image?url=%2Fimages%2Ficon_intro.svg&w=640&q=75"

            // generate JWT for register
            val jwt = generateOAuth2UserToken(email, image)

            // return redirect response
            response.addHeader(
                "Location",
                "https://waflog-web.kro.kr/register?email=$email&token=$jwt"
            )
            response.status = HttpServletResponse.SC_TEMPORARY_REDIRECT
        }
    }

    private fun generateOAuth2UserToken(email: String, image: String): String {
        val token = jwtTokenProvider.generateToken(email, join = true)
        val existingToken = verificationTokenRepository.findByEmail(email)

        existingToken?.also { // reuse token if exists: unique OAuth2UserToken
            it.token = passwordEncoder.encode(token)
            verificationTokenRepository.save(it)
        } ?: run {
            val verificationToken = VerificationToken(email, passwordEncoder.encode(token), image)
            verificationTokenRepository.save(verificationToken)
        }

        return token
    }
}
