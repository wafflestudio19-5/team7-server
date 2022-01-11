package com.wafflestudio.waflog.domain.user.exception

import com.wafflestudio.waflog.global.common.exception.ConflictException
import com.wafflestudio.waflog.global.common.exception.ErrorType

class UserIdAlreadyExistException(detail: String = "") :
    ConflictException(ErrorType.USER_ID_CONFLICT, detail)
