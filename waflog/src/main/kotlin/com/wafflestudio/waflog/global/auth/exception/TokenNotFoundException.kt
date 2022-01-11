package com.wafflestudio.waflog.global.auth.exception

import com.wafflestudio.waflog.global.common.exception.DataNotFoundException
import com.wafflestudio.waflog.global.common.exception.ErrorType

class TokenNotFoundException(detail: String = "") :
    DataNotFoundException(ErrorType.TOKEN_NOT_FOUND, detail)
