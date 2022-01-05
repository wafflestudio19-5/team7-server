package com.wafflestudio.waflog.global.auth.service

import com.wafflestudio.waflog.domain.user.exception.UserNotFoundException
import com.wafflestudio.waflog.domain.user.repository.UserRepository
import com.wafflestudio.waflog.global.auth.model.VerificationTokenPrincipal
import com.wafflestudio.waflog.global.auth.repository.VerificationTokenRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class VerificationTokenPrincipalDetailService(
    private val userRepository: UserRepository,
    private val verificationTokenRepository: VerificationTokenRepository
) : UserDetailsService {
    override fun loadUserByUsername(s: String): UserDetails {
        val user = userRepository.findByEmail(s)
            ?: throw UserNotFoundException("User with email '$s' not found")
        val token = verificationTokenRepository.findByEmail(s)
            ?: throw UsernameNotFoundException("User with email '$s' not found")
        return VerificationTokenPrincipal(user, token)
    }
}
