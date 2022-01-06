package com.wafflestudio.waflog.global.auth.model

import com.wafflestudio.waflog.domain.user.model.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class VerificationTokenPrincipal(
    val user: User?,
    val verificationToken: VerificationToken
) : UserDetails {
    override fun getUsername(): String { return verificationToken.email }

    override fun getPassword(): String { return verificationToken.token }

    override fun getAuthorities(): List<GrantedAuthority> {
        val roles: List<String> = verificationToken.role.split(",").filter { it.isNotEmpty() }
        return roles.map { role: String? -> SimpleGrantedAuthority(role) }
    }

    override fun isAccountNonExpired(): Boolean { return true }

    override fun isAccountNonLocked(): Boolean { return true }

    override fun isCredentialsNonExpired(): Boolean { return true }

    override fun isEnabled(): Boolean { return true }
}
