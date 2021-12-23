package com.wafflestudio.toy.domain.user.repository

import com.wafflestudio.toy.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long?> {
}