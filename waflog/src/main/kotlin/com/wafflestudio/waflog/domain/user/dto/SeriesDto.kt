package com.wafflestudio.waflog.domain.user.dto

import com.wafflestudio.waflog.domain.user.model.Series
import java.time.LocalDateTime

class SeriesDto {
    data class CreateRequest(
        val name: String
    )

    data class SimpleResponse(
        val id: Long,
        val name: String,
        val thumbnail: String,
        val posts: Int,
        val updatedAt: LocalDateTime?
    ) {
        constructor(series: Series) : this(
            id = series.id,
            name = series.name,
            thumbnail = if (series.posts.size != 0) {
                series.posts[0].thumbnail
            } else {
                "https://wafflestudio.com/_next/image?url=%2Fimages%2Ficon_intro.svg&w=640&q=75"
            },
            posts = series.posts.size,
            updatedAt = series.updatedAt
        )
    }
}
