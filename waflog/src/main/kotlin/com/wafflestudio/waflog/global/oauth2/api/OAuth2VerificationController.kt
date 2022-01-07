package com.wafflestudio.waflog.global.oauth2.api

import com.wafflestudio.waflog.global.auth.JwtTokenProvider
import com.wafflestudio.waflog.global.auth.dto.VerificationTokenPrincipalDto
import com.wafflestudio.waflog.global.oauth2.service.OAuth2VerificationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpSession

@RestController
@RequestMapping("/api/v1/oauth2")
class OAuth2VerificationController(
    private val oAuth2VerificationService: OAuth2VerificationService,
    private val httpSession: HttpSession,
) {
    @GetMapping("/verify")
    @ResponseStatus(HttpStatus.OK)
    fun verifySocialLogin():
        ResponseEntity<VerificationTokenPrincipalDto> {
        val token = httpSession.getAttribute("token") as String
        httpSession.removeAttribute("token")
        val userTokenPair = oAuth2VerificationService.verifyAccount(token)
        val returnResponse = VerificationTokenPrincipalDto(
            userTokenPair.first,
            userTokenPair.second
        )
        return ResponseEntity.ok()
            .body(returnResponse)
    }
}
