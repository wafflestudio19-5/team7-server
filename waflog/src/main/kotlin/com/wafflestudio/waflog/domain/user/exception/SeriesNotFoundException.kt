package com.wafflestudio.waflog.domain.user.exception

import com.wafflestudio.waflog.global.common.exception.DataNotFoundException
import com.wafflestudio.waflog.global.common.exception.ErrorType

class SeriesNotFoundException(detail: String = "") :
    DataNotFoundException(ErrorType.SERIES_NOT_FOUND, detail)
