package com.wafflestudio.waflog.global.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.wafflestudio.waflog.global.auth.dto.LoginRequest
import com.wafflestudio.waflog.global.auth.exception.VerificationTokenNotFoundException
import com.wafflestudio.waflog.global.auth.repository.VerificationTokenRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import java.io.BufferedReader
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class SignInAuthenticationFilter(
    authenticationManager: AuthenticationManager?,
    private val jwtTokenProvider: JwtTokenProvider,
    private val verificationTokenRepository: VerificationTokenRepository
) : UsernamePasswordAuthenticationFilter(authenticationManager) {
    init {
        setRequiresAuthenticationRequestMatcher(AntPathRequestMatcher("/api/v1/auth/verify/login", "GET"))
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication
    ) {
        response.addHeader("Authentication", jwtTokenProvider.generateToken(authResult))
        response.status = HttpServletResponse.SC_NO_CONTENT
    }

    override fun unsuccessfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        failed: AuthenticationException
    ) {
        super.unsuccessfulAuthentication(request, response, failed)
        response.status = HttpServletResponse.SC_UNAUTHORIZED
    }

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        val parsedRequest: LoginRequest = parseRequest(request)
        val token = verificationTokenRepository.findByToken(parsedRequest.token)
            ?: throw VerificationTokenNotFoundException("잘못된 토큰")
        val authRequest: Authentication =
            UsernamePasswordAuthenticationToken(token.email, parsedRequest.token)
        return authenticationManager.authenticate(authRequest)
    }

    private fun parseRequest(request: HttpServletRequest): LoginRequest {
        val reader: BufferedReader = request.reader
        val objectMapper = ObjectMapper()
        return objectMapper.readValue(reader, LoginRequest::class.java)
    }
}
