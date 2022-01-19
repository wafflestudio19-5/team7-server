package com.wafflestudio.waflog.domain.tag.dto

import com.wafflestudio.waflog.domain.tag.model.Tag

class TagDto {
    data class TagInPostResponse(
        val id: Long,
        val name: String,
        val url: String
    ) {
        constructor(tag: Tag) : this(
            id = tag.id,
            name = tag.name,
            url = tag.url
        )
    }

    data class TagResponse(
        val id: Long,
        val name: String,
        val url: String,
        val count: Int
    ) {
        constructor(tag: Tag) : this(
            id = tag.id,
            name = tag.name,
            url = tag.url,
            count = tag.trending
        )
    }
}
