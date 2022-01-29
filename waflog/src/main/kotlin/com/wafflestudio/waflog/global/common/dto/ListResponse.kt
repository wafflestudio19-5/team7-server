package com.wafflestudio.waflog.global.common.dto

data class ListResponse<T>(
    val count: Int,
    val value: Long = 0,
    val contents: List<T>
) {
    constructor(list: List<T>?) : this(
        count = list?.size ?: 0,
        contents = list.orEmpty()
    )
}
