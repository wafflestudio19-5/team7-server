package com.wafflestudio.waflog.domain.image.exception

import com.wafflestudio.waflog.global.common.exception.ErrorType
import com.wafflestudio.waflog.global.common.exception.InvalidRequestException

class InvalidImageFormException(detail: String = "") :
    InvalidRequestException(ErrorType.INVALID_IMAGE_FORM, detail)
