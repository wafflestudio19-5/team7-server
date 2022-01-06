package com.wafflestudio.waflog.domain.post.exception

import com.wafflestudio.waflog.global.common.exception.ErrorType
import com.wafflestudio.waflog.global.common.exception.NotAllowedException

class CommentNotWrittenByUserException(detail: String) :
    NotAllowedException(ErrorType.COMMENT_NOT_WRITTEN_BY_USER, detail)
