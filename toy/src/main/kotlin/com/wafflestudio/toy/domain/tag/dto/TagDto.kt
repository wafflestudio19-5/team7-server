package com.wafflestudio.toy.domain.tag.dto

import com.wafflestudio.toy.domain.tag.model.Tag

class TagDto {
    data class TagResponse(
        val id: Long,
        val name: String
    ) {
        constructor(tag: Tag) : this(
            id = tag.id,
            name = tag.name
        )
    }
}
