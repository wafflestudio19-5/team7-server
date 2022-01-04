package com.wafflestudio.waflog.global.auth.service

import com.wafflestudio.waflog.global.auth.model.VerificationTokenPrincipal
import com.wafflestudio.waflog.global.auth.repository.VerificationTokenRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class VerificationTokenPrincipalDetailService(private val verificationTokenRepository: VerificationTokenRepository) :
    UserDetailsService {
    override fun loadUserByUsername(s: String): UserDetails {
        val user = verificationTokenRepository.findByEmail(s)
            ?: throw UsernameNotFoundException("User with email '$s' not found")
        return VerificationTokenPrincipal(user)
    }
}
