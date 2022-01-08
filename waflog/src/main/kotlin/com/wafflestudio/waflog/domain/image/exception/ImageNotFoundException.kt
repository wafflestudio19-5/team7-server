package com.wafflestudio.waflog.domain.image.exception

import com.wafflestudio.waflog.global.common.exception.DataNotFoundException
import com.wafflestudio.waflog.global.common.exception.ErrorType

class ImageNotFoundException(detail: String = "") :
    DataNotFoundException(ErrorType.COMMENT_NOT_FOUND, detail)
