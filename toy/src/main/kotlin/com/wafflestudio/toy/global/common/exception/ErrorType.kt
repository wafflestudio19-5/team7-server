package com.wafflestudio.toy.global.common.exception

enum class ErrorType (
    val code: Int
) {
    INVALID_REQUEST(0),

    NOT_ALLOWED(3000),

    DATA_NOT_FOUND(4000),
    POST_NOT_FOUND(4001),

    CONFLICT(9000),

    SERVER_ERROR(10000)
}
