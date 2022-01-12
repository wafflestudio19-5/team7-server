package com.wafflestudio.waflog.domain.image.exception

import com.wafflestudio.waflog.global.common.exception.ErrorType
import com.wafflestudio.waflog.global.common.exception.InvalidRequestException

class ImageNotUploadedException(detail: String = "") :
    InvalidRequestException(ErrorType.IMAGE_NOT_FOUND, detail)
