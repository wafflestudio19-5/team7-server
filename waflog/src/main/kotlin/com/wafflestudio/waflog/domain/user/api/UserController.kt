package com.wafflestudio.waflog.domain.user.api

import com.wafflestudio.waflog.domain.user.dto.SeriesDto
import com.wafflestudio.waflog.domain.user.model.User
import com.wafflestudio.waflog.domain.user.service.UserService
import com.wafflestudio.waflog.global.auth.CurrentUser
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val userService: UserService
) {
    @PostMapping("/series")
    @ResponseStatus(HttpStatus.CREATED)
    fun addSeries(@RequestBody createRequest: SeriesDto.CreateRequest, @CurrentUser user: User) {
        userService.addSeries(createRequest, user)
    }
}
