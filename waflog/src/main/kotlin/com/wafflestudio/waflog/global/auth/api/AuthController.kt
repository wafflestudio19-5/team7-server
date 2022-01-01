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
    fun signUpEmail(@RequestBody signupEmailRequest: UserDto.SignUpEmailRequest): ExistUserDto.Response {
        return ExistUserDto.Response(authService.signupEmail(signupEmailRequest))
    }

    @GetMapping("/verify")
    @ResponseStatus(HttpStatus.OK)
    fun verifyAccount(@RequestParam(value = "token") token: String) {
        authService.verifyAccount(token)
    }

    @PostMapping("/user/info")
    @ResponseStatus(HttpStatus.CREATED)
    fun signUp(@RequestBody signupRequest: UserDto.SignUpRequest) {
        authService.signup(signupRequest)
    }
}
