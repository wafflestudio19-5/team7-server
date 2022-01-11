package com.wafflestudio.waflog.global.auth.exception

import com.wafflestudio.waflog.global.common.exception.ErrorType
import com.wafflestudio.waflog.global.common.exception.InvalidRequestException

class JWTInvalidException(detail: String = "") :
    InvalidRequestException(ErrorType.INVALID_JWT, detail)
