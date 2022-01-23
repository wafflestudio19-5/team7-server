package com.wafflestudio.waflog.domain.save.dto

import com.wafflestudio.waflog.domain.image.dto.ImageDto
import com.wafflestudio.waflog.domain.save.model.Save

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
}
