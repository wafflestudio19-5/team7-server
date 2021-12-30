package com.wafflestudio.waflog.global.auth.api

import com.wafflestudio.waflog.domain.user.dto.UserDto
import com.wafflestudio.waflog.global.auth.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/user/")
    @ResponseStatus(HttpStatus.CREATED)
    fun signUpEmail(@RequestBody signupEmailRequest: UserDto.SignUpEmailRequest) {
        authService.signupEmail(signupEmailRequest)
    }

    @GetMapping("/verify/{token}/")
    @ResponseStatus(HttpStatus.OK)
    fun verifyAccount(@PathVariable token: String) {
        authService.verifyAccount(token)
    }

    @PostMapping("/user/signup/")
    @ResponseStatus(HttpStatus.OK)
    fun signUp(@RequestBody signupRequest: UserDto.SignUpRequest) {
        authService.signup(signupRequest)
    }
}
