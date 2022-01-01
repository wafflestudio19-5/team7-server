package com.wafflestudio.waflog.global.auth.service

import com.wafflestudio.waflog.domain.user.dto.UserDto
import com.wafflestudio.waflog.domain.user.model.User
import com.wafflestudio.waflog.domain.user.repository.UserRepository
import com.wafflestudio.waflog.global.auth.exception.EmailNotFoundException
import com.wafflestudio.waflog.global.auth.exception.TokenNotFoundException
import com.wafflestudio.waflog.global.auth.model.VerificationToken
import com.wafflestudio.waflog.global.auth.repository.VerificationTokenRepository
import com.wafflestudio.waflog.global.mail.dto.MailDto
import com.wafflestudio.waflog.global.mail.service.MailContentBuilder
import com.wafflestudio.waflog.global.mail.service.MailService
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val mailService: MailService,
    private val verificationTokenRepository: VerificationTokenRepository,
    private val mailContentBuilder: MailContentBuilder
) {
    fun signupEmail(signUpEmailRequest: UserDto.SignUpEmailRequest): Boolean {
        val email = signUpEmailRequest.email
        val token = generateVerificationToken(email)
        userRepository.findByEmail(email)
            ?: return run {
                val link = "https://d259mvltzqd1q5.cloudfront.net/register?code=$token"
                val message = mailContentBuilder.build(link)
                val mail = MailDto.Email(email, "Waflog 회원가입", message, false)
                mailService.sendMail(mail)
                true
            }
        return run {
            val link = "https://d259mvltzqd1q5.cloudfront.net/email-login?code=$token"
            val message = mailContentBuilder.build(link)
            val mail = MailDto.Email(email, "Waflog 로그인", message, false)
            mailService.sendMail(mail)
            false
        }
    }

    fun signup(signUpRequest: UserDto.SignUpRequest) {
        val email = signUpRequest.email
        val name = signUpRequest.name
        val userId = signUpRequest.userId.lowercase()
        val shortIntro = signUpRequest.shortIntro

        val token = verificationTokenRepository.findByEmail(email)
            ?: throw EmailNotFoundException("User with $email not found")

        val user = User(
            email = email,
            userId = userId,
            name = name,
            shortIntro = shortIntro,
            pageTitle = "${userId}.log"
        )

        verificationTokenRepository.deleteById(token.id)
        userRepository.save(user)
    }

    private fun generateVerificationToken(email: String): String {
        val token = UUID.randomUUID().toString()
        val verificationToken = VerificationToken(email, token)
        verificationTokenRepository.save(verificationToken)
        return token
    }

    fun verifyAccount(token: String) {
        verificationTokenRepository.findByToken(token)
            ?: throw TokenNotFoundException("잘못된 토큰")
    }
}
