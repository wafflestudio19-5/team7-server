package com.wafflestudio.waflog.global.auth.service

import com.wafflestudio.waflog.domain.user.repository.UserRepository
import com.wafflestudio.waflog.global.auth.JwtTokenProvider
import com.wafflestudio.waflog.global.auth.exception.JWTInvalidException
import com.wafflestudio.waflog.global.auth.model.VerificationToken
import com.wafflestudio.waflog.global.auth.model.VerificationTokenPrincipal
import com.wafflestudio.waflog.global.auth.repository.VerificationTokenRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class VerificationTokenPrincipalDetailService(
    private val userRepository: UserRepository,
    private val verificationTokenRepository: VerificationTokenRepository,
    private val jwtTokenProvider: JwtTokenProvider
) : UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails {
        val user = userRepository.findByEmail(email)
        val verificationToken = verificationTokenRepository.findByEmail(email)
            ?: throw UsernameNotFoundException("User with email '$email' not found")
        return VerificationTokenPrincipal(user, verificationToken)
    }
}
