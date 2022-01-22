package com.wafflestudio.waflog.domain.save.repository

import com.wafflestudio.waflog.domain.save.model.SaveToken
import com.wafflestudio.waflog.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface SaveTokenRepository : JpaRepository<SaveToken, Long?> {

    fun findByTokenAndSave_User(token: String, user: User): SaveToken?
}
