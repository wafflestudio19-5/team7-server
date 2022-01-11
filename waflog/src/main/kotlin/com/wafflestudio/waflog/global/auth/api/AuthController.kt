package com.wafflestudio.waflog.global.auth.api

import com.wafflestudio.waflog.domain.user.dto.UserDto
import com.wafflestudio.waflog.global.auth.JwtTokenProvider
import com.wafflestudio.waflog.global.auth.dto.ExistUserDto
import com.wafflestudio.waflog.global.auth.dto.VerificationTokenPrincipalDto
import com.wafflestudio.waflog.global.auth.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService,
    private val jwtTokenProvider: JwtTokenProvider
) {
    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    fun signUpEmail(@RequestBody joinEmailRequest: UserDto.JoinEmailRequest): ExistUserDto.Response {
        return ExistUserDto.Response(authService.signUpEmail(joinEmailRequest))
    }

    @PostMapping("/user/login")
    @ResponseStatus(HttpStatus.CREATED)
    fun signInEmail(@RequestBody joinEmailRequest: UserDto.JoinEmailRequest): ExistUserDto.Response {
        return ExistUserDto.Response(authService.signInEmail(joinEmailRequest))
    }

    @PostMapping("/user/info")
    @ResponseStatus(HttpStatus.CREATED)
    fun signUp(@RequestBody signupRequest: UserDto.SignUpRequest): VerificationTokenPrincipalDto {
        return authService.signUp(signupRequest)
    }
}
