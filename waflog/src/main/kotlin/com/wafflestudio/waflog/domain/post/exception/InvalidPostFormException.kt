package com.wafflestudio.waflog.domain.post.exception

import com.wafflestudio.waflog.global.common.exception.ErrorType
import com.wafflestudio.waflog.global.common.exception.InvalidRequestException

class InvalidPostFormException(detail: String = "") :
    InvalidRequestException(ErrorType.INVALID_REQUEST, detail)
