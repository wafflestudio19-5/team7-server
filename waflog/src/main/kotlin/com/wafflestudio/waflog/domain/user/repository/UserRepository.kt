package com.wafflestudio.waflog.domain.user.repository

import com.wafflestudio.waflog.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long?> {
    fun findByEmail(email: String): User?
    fun existsByUserId(userId: String): Boolean
    fun findByUserId(userId: String): User?
}
