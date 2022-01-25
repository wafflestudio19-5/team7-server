package com.wafflestudio.waflog.domain.image.repository

import com.wafflestudio.waflog.domain.image.model.Image
import org.springframework.data.jpa.repository.JpaRepository

interface ImageRepository : JpaRepository<Image, Long?> {

    fun findByUser_UserIdAndToken(userId: String, token: String): Image?

    fun findAllByUser_UserIdAndPost_Id(userId: String, id: Long): List<Image>

    fun findAllByUser_Id(userId: Long): List<Image>

    fun findAllByUser_UserIdAndSave_Id(userId: String, id: Long): List<Image>
}
