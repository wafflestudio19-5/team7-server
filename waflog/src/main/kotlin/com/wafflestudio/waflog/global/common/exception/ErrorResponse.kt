package com.wafflestudio.waflog.global.common.exception

data class ErrorResponse(
    val errorCode: Int,
    val errorMessage: String = "",
    val detail: String = ""
)
