package com.wafflestudio.waflog.global.auth.service

import com.wafflestudio.waflog.domain.user.dto.UserDto
import com.wafflestudio.waflog.domain.user.model.User
import com.wafflestudio.waflog.domain.user.repository.UserRepository
import com.wafflestudio.waflog.global.auth.JwtTokenProvider
import com.wafflestudio.waflog.global.auth.dto.VerificationTokenPrincipalDto
import com.wafflestudio.waflog.global.auth.exception.JWTInvalidException
import com.wafflestudio.waflog.global.auth.model.SignUpAttempt
import com.wafflestudio.waflog.global.auth.model.VerificationToken
import com.wafflestudio.waflog.global.auth.repository.SignUpAttemptRepository
import com.wafflestudio.waflog.global.auth.repository.VerificationTokenRepository
import com.wafflestudio.waflog.global.mail.dto.MailDto
import com.wafflestudio.waflog.global.mail.service.MailContentBuilder
import com.wafflestudio.waflog.global.mail.service.MailService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val mailService: MailService,
    private val verificationTokenRepository: VerificationTokenRepository,
    private val signUpAttemptRepository: SignUpAttemptRepository,
    private val mailContentBuilder: MailContentBuilder,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider
) {
    fun signUpEmail(joinEmailRequest: UserDto.JoinEmailRequest): Boolean {
        val email = joinEmailRequest.email
        userRepository.findByEmail(email)
            ?: return run {
                val jwt = generateSignUpJWT(email)
                val link = "https://waflog-web.kro.kr/register?email=$email&token=$jwt"
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

    fun signUp(signUpRequest: UserDto.SignUpRequest): VerificationTokenPrincipalDto {
        val email = signUpRequest.email
        val name = signUpRequest.name
        val userId = signUpRequest.userId.lowercase()
        val shortIntro = signUpRequest.shortIntro
        val jwt = signUpRequest.token

        if (!jwtTokenProvider.validateToken(jwt))
            throw JWTInvalidException("JWT is invalid")
        val attempt = signUpAttemptRepository.findByJwt(jwt)
            ?: throw JWTInvalidException("JWT does not correspond to any sign up request")
        val jwtEmail = jwtTokenProvider.getEmailFromJwt(jwt)
        if (attempt.email != jwtEmail)
            throw JWTInvalidException("JWT does not correspond to the email")

        val user = userRepository.save(
            User(
                email = email,
                userId = userId,
                name = name,
                image = attempt.image,
                shortIntro = shortIntro,
                pageTitle = "$userId.log"
            )
        )
        signUpAttemptRepository.deleteById(attempt.id)

        val token = generateVerificationToken(user)

        return VerificationTokenPrincipalDto(UserDto.SimpleResponse(user), token)
    }

    private fun generateSignUpJWT(email: String): String {
        val jwt = jwtTokenProvider.generateToken(email, signup = true)
        val existingAttempt = signUpAttemptRepository.findByEmail(email)

        existingAttempt?.also {
            it.jwt = jwt
            signUpAttemptRepository.save(it)
        } ?: run {
            signUpAttemptRepository.save(SignUpAttempt(email = email, jwt = jwt))
        }

        return jwt
    }

    private fun generateVerificationToken(user: User): String {
        val token = UUID.randomUUID().toString()
        val existingToken = verificationTokenRepository.findByEmail(user.email)

        existingToken?.also {
            it.token = passwordEncoder.encode(token)
            verificationTokenRepository.save(it)
        } ?: run {
            val verificationToken = VerificationToken(user.email, passwordEncoder.encode(token))
            verificationTokenRepository.save(verificationToken)
        }

        return token
    }
}
