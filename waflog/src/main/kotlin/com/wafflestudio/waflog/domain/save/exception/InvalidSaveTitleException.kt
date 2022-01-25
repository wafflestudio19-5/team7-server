package com.wafflestudio.waflog.domain.save.exception

import com.wafflestudio.waflog.global.common.exception.ErrorType
import com.wafflestudio.waflog.global.common.exception.InvalidRequestException

class InvalidSaveTitleException(detail: String = "") :
    InvalidRequestException(ErrorType.INVALID_SAVE_TITLE, detail)
