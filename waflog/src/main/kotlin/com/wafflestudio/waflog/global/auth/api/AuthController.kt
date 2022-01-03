package com.wafflestudio.waflog.global.auth.api

import com.wafflestudio.waflog.domain.user.dto.UserDto
import com.wafflestudio.waflog.global.auth.JwtTokenProvider
import com.wafflestudio.waflog.global.auth.dto.ExistUserDto
import com.wafflestudio.waflog.global.auth.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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

    @GetMapping("/verify")
    @ResponseStatus(HttpStatus.OK)
    fun verifyAccount(@RequestParam(value = "token", required = true) token: String):
        ResponseEntity<UserDto.SimpleResponse> {
        val email = authService.verifyAccount(token)
        return ResponseEntity.noContent().header("Authentication", jwtTokenProvider.generateToken(email)).build()
    }

    @PostMapping("/user/info")
    @ResponseStatus(HttpStatus.CREATED)
    fun signUp(@RequestBody signupRequest: UserDto.SignUpRequest) {
        authService.signUp(signupRequest)
    }
}
