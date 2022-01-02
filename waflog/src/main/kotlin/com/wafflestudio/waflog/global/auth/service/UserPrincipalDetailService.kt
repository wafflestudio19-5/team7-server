package com.wafflestudio.waflog.global.auth.service

import com.wafflestudio.waflog.domain.user.repository.UserRepository
import com.wafflestudio.waflog.global.auth.model.UserPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserPrincipalDetailService(private val userRepository: UserRepository) : UserDetailsService {
    override fun loadUserByUsername(s: String): UserDetails {
        val user = userRepository.findByEmail(s) ?: throw UsernameNotFoundException("User with email '%s' not found")
        return UserPrincipal(user)
    }
}
