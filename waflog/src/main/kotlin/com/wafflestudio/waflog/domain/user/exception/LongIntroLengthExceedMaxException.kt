package com.wafflestudio.waflog.domain.user.exception

import com.wafflestudio.waflog.global.common.exception.ErrorType
import com.wafflestudio.waflog.global.common.exception.InvalidRequestException

class LongIntroLengthExceedMaxException(detail: String = "")
    : InvalidRequestException(ErrorType.USER_LONG_INTRO_LENGTH_EXCEED, detail)

