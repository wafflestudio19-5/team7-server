package com.wafflestudio.waflog.global.auth.api

import com.wafflestudio.waflog.domain.user.dto.UserDto
import com.wafflestudio.waflog.global.auth.dto.ExistUserDto
import com.wafflestudio.waflog.global.auth.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    fun signUpEmail(@RequestBody joinEmailRequest: UserDto.JoinEmailRequest): ExistUserDto.Response {
        return ExistUserDto.Response(authService.signUpEmail(joinEmailRequest))
    }

    @PostMapping("/user/login")
    @ResponseStatus(HttpStatus.OK)
    fun signInEmail(@RequestBody joinEmailRequest: UserDto.JoinEmailRequest): ExistUserDto.Response {
        return ExistUserDto.Response(authService.signInEmail(joinEmailRequest))
    }

    @GetMapping("/verify")
    @ResponseStatus(HttpStatus.OK)
    fun verifyNewAccount(@RequestParam(value = "token") token: String) {
        authService.verifyAccount(token)
    }

    @GetMapping("/verify/login")
    @ResponseStatus(HttpStatus.OK)
    fun verifyExistAccount(@RequestParam(value = "token") token: String): UserDto.SimpleResponse {
        return authService.signIn(token)
    }

    @PostMapping("/user/info")
    @ResponseStatus(HttpStatus.CREATED)
    fun signUp(@RequestBody signupRequest: UserDto.SignUpRequest) {
        authService.signUp(signupRequest)
    }
}
