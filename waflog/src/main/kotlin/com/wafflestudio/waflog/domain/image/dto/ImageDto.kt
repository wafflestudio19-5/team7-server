package com.wafflestudio.waflog.domain.image.dto

class ImageDto {

    data class CreateResponse(
        val token: String,
        val url: String
    ) {
        constructor(pair: Pair<String, String>) : this(
            token = pair.first,
            url = pair.second
        )
    }

    data class RemoveRequest(
        val token: String
    )

    data class S3Token(
        val token: String
    )
}
