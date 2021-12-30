package com.wafflestudio.toy.global.auth.service

import com.wafflestudio.toy.domain.user.dto.UserDto
import com.wafflestudio.toy.domain.user.model.User
import com.wafflestudio.toy.domain.user.repository.UserRepository
import com.wafflestudio.toy.global.auth.model.VerificationToken
import com.wafflestudio.toy.global.auth.repository.VerificationTokenRepository
import com.wafflestudio.toy.global.mail.dto.MailDto
import com.wafflestudio.toy.global.mail.service.MailContentBuilder
import com.wafflestudio.toy.global.mail.service.MailService
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val mailService: MailService,
    private val verificationTokenRepository: VerificationTokenRepository,
    private val mailContentBuilder: MailContentBuilder
) {
    fun signupEmail(signUpEmailRequest: UserDto.SignUpEmailRequest) {
        val email = signUpEmailRequest.email

        val user = User(email)
        userRepository.save(user)

        val token = generateVerificationToken(user)
        val link = "https://d259mvltzqd1q5.cloudfront.net/register?code=$token"
        val message = mailContentBuilder.build(link)
        val mail = MailDto.Email(email, "Waflog 회원가입", message, true)
        mailService.sendMail(mail)
    }

    fun signup(signUpRequest: UserDto.SignUpRequest) {
        val verificationTokenOptional = verificationTokenRepository.findByToken(signUpRequest.token)
        //        verificationTokenOptional.orElseThrow { CustomException("잘못된 토큰") }
        val user = verificationTokenOptional!!.user
        user.username = signUpRequest.username
        user.userId = signUpRequest.userid
        user.intro = signUpRequest.intro

        userRepository.save(user)
    }

    private fun generateVerificationToken(user: User): String {
        val token = UUID.randomUUID().toString()
        val verificationToken = VerificationToken(user, token)
        verificationTokenRepository.save(verificationToken)
        return token
    }

    fun verifyAccount(token: String) {
        val verificationTokenOptional = verificationTokenRepository.findByToken(token)
//        verificationTokenOptional.orElseThrow { CustomException("잘못된 토큰") }
        fetchUserAndEnable(verificationTokenOptional!!)
    }

    private fun fetchUserAndEnable(verificationToken: VerificationToken) {
        val email = verificationToken.user.email
        val user = userRepository.findByEmail(email)!!
//            .orElseThrow { CustomException("유저를 찾을 수 없음 $email") }
        user.enabled = true
        userRepository.save(user)
    }
}
