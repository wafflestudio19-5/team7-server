package com.wafflestudio.toy.global.auth.repository

import com.wafflestudio.toy.global.auth.model.VerificationToken
import org.springframework.data.jpa.repository.JpaRepository

interface VerificationTokenRepository : JpaRepository<VerificationToken, Long?>  {
    fun findByToken(token: String?): VerificationToken?

}