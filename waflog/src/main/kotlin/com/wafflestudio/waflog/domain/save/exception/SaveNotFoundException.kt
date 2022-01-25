package com.wafflestudio.waflog.domain.save.exception

import com.wafflestudio.waflog.global.common.exception.ErrorType
import com.wafflestudio.waflog.global.common.exception.InvalidRequestException

class SaveNotFoundException(detail: String = "") :
    InvalidRequestException(ErrorType.SAVE_NOT_FOUND, detail)
