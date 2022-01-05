package com.wafflestudio.waflog.domain.post.exception

import com.wafflestudio.waflog.global.common.exception.DataNotFoundException
import com.wafflestudio.waflog.global.common.exception.ErrorType

class CommentNotFoundException(detail: String = "") :
    DataNotFoundException(ErrorType.COMMENT_NOT_FOUND, detail)
