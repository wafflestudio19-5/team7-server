package com.wafflestudio.waflog.domain.user.exception

import com.wafflestudio.waflog.global.common.exception.ErrorType
import com.wafflestudio.waflog.global.common.exception.InvalidRequestException

class InvalidUserPageTitleException(detail: String = "") :
    InvalidRequestException(ErrorType.INVALID_USER_PAGE_TITLE, detail)
