package com.wafflestudio.toy.global.common.exception

import com.fasterxml.jackson.annotation.JsonProperty

data class ErrorResponse(
    @JsonProperty("errorCode")
    val errorCode: Int,
    @JsonProperty("errorMessage")
    val errorMessage: String = "",
    val detail: String = ""
)
