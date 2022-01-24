package com.wafflestudio.waflog.domain.save.dto

import com.wafflestudio.waflog.domain.image.dto.ImageDto
import com.wafflestudio.waflog.domain.save.model.Save
import com.wafflestudio.waflog.domain.save.model.SaveToken
import java.time.LocalDateTime

class SaveDto {
    data class CreateRequest(
        val title: String,
        val content: String,
        val images: List<ImageDto.S3Token>,
        val tags: List<String>
    )

    data class PutRequest(
        val token: String,
        val title: String,
        val content: String,
        val images: List<ImageDto.S3Token>,
        val tags: List<String>
    )

    data class Response(
        val title: String,
        val content: String,
        val images: List<ImageDto.S3Token>,
        val tags: List<String>
    ) {
        constructor(save: Save) : this(
            title = save.title,
            content = save.content,
            images = save.images.map { ImageDto.S3Token(it.token) },
            tags = save.saveTags
        )
    }

    data class SimpleResponse(
        val token: String,
        val title: String,
        val shortContent: String,
        val createAt: LocalDateTime?
    ) {
        constructor(saveToken: SaveToken) : this(
            token = saveToken.token,
            title = saveToken.save.title,
            shortContent = saveToken.save.content.let { if (it.length > 200) it.take(200) + "..." else it },
            createAt = saveToken.save.createdAt
        )
    }
}
