package com.wafflestudio.waflog.domain.tag.exception

import com.wafflestudio.waflog.global.common.exception.DataNotFoundException
import com.wafflestudio.waflog.global.common.exception.ErrorType

class TagNotFoundException(detail: String = "") :
    DataNotFoundException(ErrorType.TAG_NOT_FOUND, detail)
