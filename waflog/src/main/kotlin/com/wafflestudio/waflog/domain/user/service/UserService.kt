package com.wafflestudio.waflog.domain.user.service

import com.wafflestudio.waflog.domain.user.dto.SeriesDto
import com.wafflestudio.waflog.domain.user.exception.SeriesUrlExistException
import com.wafflestudio.waflog.domain.user.model.Series
import com.wafflestudio.waflog.domain.user.model.User
import com.wafflestudio.waflog.domain.user.repository.SeriesRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val seriesRepository: SeriesRepository
) {
    fun addSeries(createRequest: SeriesDto.CreateRequest, user: User) {
        val seriesName = createRequest.name
        seriesRepository.findByNameAndUser(seriesName, user)
            ?: run {
                val newSeries = Series(user, seriesName, mutableListOf())
                seriesRepository.save(newSeries)
                return
            }
        throw SeriesUrlExistException("이미 존재하는 URL입니다.")
    }
}
