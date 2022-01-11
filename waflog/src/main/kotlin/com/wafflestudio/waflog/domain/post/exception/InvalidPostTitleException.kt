package com.wafflestudio.waflog.domain.post.exception

import com.wafflestudio.waflog.global.common.exception.ErrorType
import com.wafflestudio.waflog.global.common.exception.InvalidRequestException

class InvalidPostTitleException(detail: String = "") :
    InvalidRequestException(ErrorType.INVALID_POST_TITLE, detail)
