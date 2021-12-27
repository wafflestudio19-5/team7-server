package com.wafflestudio.toy.domain.post.exception

import com.wafflestudio.toy.global.common.exception.DataNotFoundException
import com.wafflestudio.toy.global.common.exception.ErrorType

class PostNotFoundException(detail: String = "") :
    DataNotFoundException(ErrorType.POST_NOT_FOUND, detail)