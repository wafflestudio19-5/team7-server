package com.wafflestudio.waflog.domain.user.exception

import com.wafflestudio.waflog.global.common.exception.ErrorType
import com.wafflestudio.waflog.global.common.exception.InvalidRequestException

class SeriesExistException(detail: String = "") :
    InvalidRequestException(ErrorType.INVALID_REQUEST, detail)
