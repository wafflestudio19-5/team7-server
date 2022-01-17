package com.wafflestudio.waflog.domain.user.repository

import com.wafflestudio.waflog.domain.user.model.Reads
import org.springframework.data.jpa.repository.JpaRepository

interface ReadsRepository : JpaRepository<Reads, Long?> {
    fun findByUser_UserIdAndReadPost_Id(userId: String, postId: Long): Reads?
}
