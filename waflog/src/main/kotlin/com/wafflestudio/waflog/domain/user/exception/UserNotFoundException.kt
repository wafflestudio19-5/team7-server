package com.wafflestudio.waflog.domain.user.exception

import com.wafflestudio.waflog.global.common.exception.DataNotFoundException
import com.wafflestudio.waflog.global.common.exception.ErrorType

class UserNotFoundException(detail: String = "") :
    DataNotFoundException(ErrorType.DATA_NOT_FOUND, detail)
