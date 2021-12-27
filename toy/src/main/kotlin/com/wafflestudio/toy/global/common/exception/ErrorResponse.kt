package com.wafflestudio.toy.global.common.exception

import com.fasterxml.jackson.annotation.JsonProperty

data class ErrorResponse(
    val errorCode: Int,
    val errorMessage: String = "",
    val detail: String = ""
)
