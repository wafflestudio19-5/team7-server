package com.wafflestudio.waflog.domain.image.dto

class ImageDto {

    data class S3URL(
        val url: String
    )

    data class RemoveRequest(
        val token: String
    )
}
