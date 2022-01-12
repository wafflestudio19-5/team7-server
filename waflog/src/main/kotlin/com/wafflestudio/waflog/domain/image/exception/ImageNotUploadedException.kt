package com.wafflestudio.waflog.domain.image.exception

import com.wafflestudio.waflog.global.common.exception.ConflictException
import com.wafflestudio.waflog.global.common.exception.ErrorType

class ImageNotUploadedException(detail: String = "") :
    ConflictException(ErrorType.SERVER_ERROR, detail)
