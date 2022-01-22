package com.wafflestudio.waflog.domain.save.dto

import com.wafflestudio.waflog.domain.image.dto.ImageDto

class SaveDto {
    data class CreateRequest(
        val title: String,
        val content: String,
        val images: List<ImageDto.S3Token>,
        val tags: List<String>
    )
}
