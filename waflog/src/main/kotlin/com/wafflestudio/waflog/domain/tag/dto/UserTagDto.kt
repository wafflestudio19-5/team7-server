package com.wafflestudio.waflog.domain.tag.dto

import com.wafflestudio.waflog.domain.tag.model.Tag

data class UserTagDto(
    val id: Long,
    val name: String,
    val url: String,
    val count: Long
) {
    constructor(tag: Tag, tagCount: Long) : this(
        id = tag.id,
        name = tag.name,
        url = tag.url,
        count = tagCount
    )
}

