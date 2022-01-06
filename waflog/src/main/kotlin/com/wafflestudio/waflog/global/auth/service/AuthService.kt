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
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val mailService: MailService,
    private val verificationTokenRepository: VerificationTokenRepository,
    private val mailContentBuilder: MailContentBuilder,
    private val passwordEncoder: PasswordEncoder
) {
    fun signUpEmail(joinEmailRequest: UserDto.JoinEmailRequest): Boolean {
        val email = joinEmailRequest.email
        userRepository.findByEmail(email)
            ?: return run {
                val token = generateVerificationToken(email)
                val link = "https://waflog-web.kro.kr/register?email=$email&code=$token"
                val message = mailContentBuilder.build(link)
                val mail = MailDto.Email(email, "Waflog 회원가입", message, false)
                mailService.sendMail(mail)
                false // new user
            }
        return signInEmail(joinEmailRequest)
    }

    fun signInEmail(joinEmailRequest: UserDto.JoinEmailRequest): Boolean {
        val email = joinEmailRequest.email
        val user = userRepository.findByEmail(email)
            ?: return signUpEmail(joinEmailRequest)
        return run {
            val token = generateVerificationToken(user)
            val link = "https://waflog-web.kro.kr/email-login?email=$email&code=$token"
            val message = mailContentBuilder.build(link)
            val mail = MailDto.Email(email, "Waflog 로그인", message, false)
            mailService.sendMail(mail)
            true // exist user
        }
    }

    fun signUp(signUpRequest: UserDto.SignUpRequest) {
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
            pageTitle = "$userId.log"
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

    private fun generateVerificationToken(user: User): String {
        val token = UUID.randomUUID().toString()
        val verificationToken = VerificationToken(user.email, passwordEncoder.encode(token))
        verificationTokenRepository.save(verificationToken)
        return token
    }

    fun verifyAccount(token: String): String {
        val verificationToken = verificationTokenRepository.findByToken(token)
            ?: throw TokenNotFoundException("잘못된 토큰")
        return verificationToken.email
    }
}
