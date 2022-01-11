package com.wafflestudio.waflog.domain.image.repository

import com.wafflestudio.waflog.domain.image.model.Image
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ImageRepository : JpaRepository<Image, Long?> {

    @Query("SELECT i FROM Image i WHERE i.userId = :userId AND i.token = :token")
    fun findByUserIdAndToken(@Param("userId") userId: String, @Param("token") token: String): Image?
}
