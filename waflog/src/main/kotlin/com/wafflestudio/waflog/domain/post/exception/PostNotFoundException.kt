package com.wafflestudio.waflog.domain.post.exception

import com.wafflestudio.waflog.global.common.exception.DataNotFoundException
import com.wafflestudio.waflog.global.common.exception.ErrorType

class PostNotFoundException(detail: String = "") :
    DataNotFoundException(ErrorType.POST_NOT_FOUND, detail)
