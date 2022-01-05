package com.wafflestudio.waflog.global.common.exception

enum class ErrorType(
    val code: Int
) {
    INVALID_REQUEST(0),
    PARENT_COMMENT_NOT_IN_SAME_POST(11),

    NOT_ALLOWED(3000),
    COMMENT_NOT_WRITTEN_BY_USER(3010),

    DATA_NOT_FOUND(4000),
    POST_NOT_FOUND(4001),
    COMMENT_NOT_FOUND(4002),

    CONFLICT(9000),

    SERVER_ERROR(10000)
}
