package com.wafflestudio.toy.domain.user.api

import com.wafflestudio.toy.domain.user.dto.UserDto
import com.wafflestudio.toy.global.auth.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
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
