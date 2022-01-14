package com.wafflestudio.waflog.global.common.exception

enum class ErrorType(
    val code: Int
) {
    INVALID_REQUEST(0),
    INVALID_POST_TITLE(1),
    INVALID_SERIES_URL(2),
    INVALID_IMAGE_FORM(3),
    INVALID_JWT(4),
    PARENT_COMMENT_NOT_IN_SAME_POST(11),
    INVALID_USER_NAME(21),
    INVALID_USER_PAGE_TITLE(22),

    NOT_ALLOWED(3000),
    COMMENT_NOT_WRITTEN_BY_USER(3010),

    DATA_NOT_FOUND(4000),
    USER_NOT_FOUND(4001),
    POST_NOT_FOUND(4002),
    COMMENT_NOT_FOUND(4003),
    IMAGE_NOT_FOUND(4004),
    SERIES_NOT_FOUND(4005),
    EMAIL_NOT_FOUND(4006),
    TOKEN_NOT_FOUND(4007),

    CONFLICT(9000),
    USER_ID_CONFLICT(9011),

    SERVER_ERROR(10000)
}
