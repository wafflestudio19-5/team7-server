package com.wafflestudio.waflog.global.auth.service

import com.wafflestudio.waflog.domain.user.dto.UserDto
import com.wafflestudio.waflog.domain.user.exception.UserIdAlreadyExistException
import com.wafflestudio.waflog.domain.user.model.User
import com.wafflestudio.waflog.domain.user.repository.UserRepository
import com.wafflestudio.waflog.global.auth.JwtTokenProvider
import com.wafflestudio.waflog.global.auth.dto.VerificationTokenPrincipalDto
import com.wafflestudio.waflog.global.auth.exception.JWTInvalidException
import com.wafflestudio.waflog.global.auth.model.VerificationToken
import com.wafflestudio.waflog.global.auth.repository.VerificationTokenRepository
import com.wafflestudio.waflog.global.mail.dto.MailDto
import com.wafflestudio.waflog.global.mail.service.MailContentBuilder
import com.wafflestudio.waflog.global.mail.service.MailService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val mailService: MailService,
    private val verificationTokenRepository: VerificationTokenRepository,
    private val mailContentBuilder: MailContentBuilder,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider
) {
    fun signUpEmail(joinEmailRequest: UserDto.JoinEmailRequest): Boolean {
        val email = joinEmailRequest.email
        userRepository.findByEmail(email)
            ?: return run {
                val jwt = generateJWT(email)
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
        userRepository.findByEmail(email)
            ?: return signUpEmail(joinEmailRequest)
        return run {
            val jwt = generateJWT(email)
            val link = "https://waflog-web.kro.kr/email-login?email=$email&token=$jwt"
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

        val verificationToken = verifyJWT(email, jwt)
        userId.let {
            if (userRepository.existsByUserId(userId))
                throw UserIdAlreadyExistException("User with this id already exists")
        }
        val user = userRepository.save(
            User(
                email = email,
                userId = userId,
                name = name,
                image = verificationToken.image,
                shortIntro = shortIntro,
                pageTitle = "$userId.log"
            )
        )
        val token = jwtTokenProvider.generateToken(email, join = false)

        return VerificationTokenPrincipalDto(UserDto.SimpleResponse(user), token)
    }

    fun verifySignUp(verifyRequest: UserDto.VerifyRequest) {
        val email = verifyRequest.email
        val jwt = verifyRequest.token

        verifyJWT(email, jwt)
    }

    fun verifyJWT(email: String, jwt: String): VerificationToken {
        if (!jwtTokenProvider.validateToken(jwt))
            throw JWTInvalidException("JWT is invalid")
        jwtTokenProvider.getEmailFromJwt(jwt)
            .let {
                if (it != email)
                    throw JWTInvalidException("JWT does not correspond to the email")
            }
        val verificationToken = verificationTokenRepository.findByEmail(email)!!
            .also {
                if (!passwordEncoder.matches(jwt, it.token))
                    throw JWTInvalidException("JWT does not correspond to any join request")
            }
        return verificationToken
    }

    private fun generateJWT(email: String): String {
        val jwt = jwtTokenProvider.generateToken(email, join = true)
        val existingToken = verificationTokenRepository.findByEmail(email)

        existingToken?.let {
            it.token = passwordEncoder.encode(jwt)
            verificationTokenRepository.save(it)
        } ?: run {
            verificationTokenRepository.save(VerificationToken(email = email, token = passwordEncoder.encode(jwt)))
        }

        return jwt
    }
}
