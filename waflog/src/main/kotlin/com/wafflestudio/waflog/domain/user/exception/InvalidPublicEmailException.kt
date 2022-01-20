package com.wafflestudio.waflog.domain.user.exception

import com.wafflestudio.waflog.global.common.exception.ErrorType
import com.wafflestudio.waflog.global.common.exception.InvalidRequestException

class InvalidPublicEmailException(detail: String = "") :
    InvalidRequestException(ErrorType.INVALID_USER_PUBLIC_EMAIL, detail)
