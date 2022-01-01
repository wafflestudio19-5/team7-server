package com.wafflestudio.waflog.global.auth.service

import com.wafflestudio.waflog.domain.user.dto.UserDto
import com.wafflestudio.waflog.domain.user.model.User
import com.wafflestudio.waflog.domain.user.repository.UserRepository
import com.wafflestudio.waflog.global.auth.exception.TokenMissMatchedException
import com.wafflestudio.waflog.global.auth.model.VerificationTokenUser
import com.wafflestudio.waflog.global.auth.repository.VerificationTokenUserRepository
import com.wafflestudio.waflog.global.mail.dto.MailDto
import com.wafflestudio.waflog.global.mail.service.MailContentBuilder
import com.wafflestudio.waflog.global.mail.service.MailService
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val mailService: MailService,
    private val verificationTokenUserRepository: VerificationTokenUserRepository,
    private val mailContentBuilder: MailContentBuilder
) {
    fun signupEmail(signUpEmailRequest: UserDto.SignUpEmailRequest): Boolean {
        val email = signUpEmailRequest.email
        val token = generateVerificationToken(email)
        val existUser = userRepository.findByEmail(email)
            ?: return run {
                val link = "https://d259mvltzqd1q5.cloudfront.net/register?code=$token"
                val message = mailContentBuilder.build(link)
                val mail = MailDto.Email(email, "Waflog 회원가입", message, true)
                mailService.sendMail(mail)
                true
            }
        return run {
            val link = "https://d259mvltzqd1q5.cloudfront.net/email-login?code=$token"
            val message = mailContentBuilder.build(link)
            val mail = MailDto.Email(email, "Waflog 로그인", message, true)
            mailService.sendMail(mail)
            false
        }
    }

    fun signup(signUpRequest: UserDto.SignUpRequest) {
        val email = signUpRequest.email
        val userName = signUpRequest.userName
        val userId = signUpRequest.userId
        val shortIntro = signUpRequest.shortIntro
        val user = User(email, userId, userName, shortIntro)
        verificationTokenUserRepository.deleteByEmail(email)
        userRepository.save(user)
    }

    private fun generateVerificationToken(email: String): String {
        val token = UUID.randomUUID().toString()
        val verificationTokenUser = VerificationTokenUser(email, token)
        verificationTokenUserRepository.save(verificationTokenUser)
        return token
    }

    fun verifyAccount(token: String) {
        verificationTokenUserRepository.findByToken(token)
            ?: throw TokenMissMatchedException("잘못된 토큰")
    }
}
