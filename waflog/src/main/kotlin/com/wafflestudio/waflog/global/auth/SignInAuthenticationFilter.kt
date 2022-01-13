package com.wafflestudio.waflog.global.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.wafflestudio.waflog.global.auth.dto.LoginRequest
import com.wafflestudio.waflog.global.auth.dto.VerificationTokenPrincipalDto
import com.wafflestudio.waflog.global.auth.exception.JWTInvalidException
import com.wafflestudio.waflog.global.auth.exception.TokenNotFoundException
import com.wafflestudio.waflog.global.auth.model.VerificationTokenPrincipal
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
    private val objectMapper: ObjectMapper
) : UsernamePasswordAuthenticationFilter(authenticationManager) {
    init {
        setRequiresAuthenticationRequestMatcher(AntPathRequestMatcher("/api/v1/auth/verify/login", "POST"))
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication
    ) {
        val jwt = jwtTokenProvider.generateToken(authResult)
        response.addHeader("Authentication", jwt)
        response.status = HttpServletResponse.SC_OK
        response.contentType = "application/json"
        response.characterEncoding = "utf-8"

        val out = response.writer

        val principal = authResult.principal as VerificationTokenPrincipal
        val userJsonString = objectMapper.writeValueAsString(VerificationTokenPrincipalDto(principal, jwt))
        out.print(userJsonString)

        out.flush()
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
        val email = parsedRequest.email
        val jwt = parsedRequest.token
        if (!jwtTokenProvider.validateToken(jwt))
            throw JWTInvalidException("JWT is invalid")
        jwtTokenProvider.getEmailFromJwt(jwt!!)
            .let {
                if (it != email)
                    throw JWTInvalidException("JWT does not correspond to the email")
            }
        val authRequest: Authentication =
            UsernamePasswordAuthenticationToken(parsedRequest.email, parsedRequest.token)
        return authenticationManager.authenticate(authRequest)
    }

    private fun parseRequest(request: HttpServletRequest): LoginRequest {
        val reader: BufferedReader = request.reader
        val objectMapper = ObjectMapper()
        return objectMapper.readValue(reader, LoginRequest::class.java)
    }
}
