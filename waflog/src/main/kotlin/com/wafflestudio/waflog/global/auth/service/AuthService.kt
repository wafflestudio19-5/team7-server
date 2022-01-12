package com.wafflestudio.waflog.global.auth.service

import com.wafflestudio.waflog.domain.user.dto.UserDto
import com.wafflestudio.waflog.domain.user.exception.UserIdAlreadyExistException
import com.wafflestudio.waflog.domain.user.model.User
import com.wafflestudio.waflog.domain.user.repository.UserRepository
import com.wafflestudio.waflog.global.auth.JwtTokenProvider
import com.wafflestudio.waflog.global.auth.dto.VerificationTokenPrincipalDto
import com.wafflestudio.waflog.global.auth.exception.JWTInvalidException
import com.wafflestudio.waflog.global.auth.model.JoinAttempt
import com.wafflestudio.waflog.global.auth.model.VerificationToken
import com.wafflestudio.waflog.global.auth.repository.JoinAttemptRepository
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
    private val joinAttemptRepository: JoinAttemptRepository,
    private val mailContentBuilder: MailContentBuilder,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider
) {
    fun signUpEmail(joinEmailRequest: UserDto.JoinEmailRequest): Boolean {
        val email = joinEmailRequest.email
        userRepository.findByEmail(email)
            ?: return run {
                val jwt = generateJoinJWT(email)
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
            val jwt = generateVerificationToken(email)
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

        val attempt = verifyJWT(email, jwt)
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
        val token = generateVerificationToken(user.email)

        return VerificationTokenPrincipalDto(UserDto.SimpleResponse(user), token)
    }

    fun signIn(signInRequest: UserDto.SignInRequest): VerificationTokenPrincipalDto {
        val email = signInRequest.email
        val jwt = signInRequest.token

        verifyJWT(email, jwt)
        val user = userRepository.findByEmail(email)!!
        val token = generateVerificationToken(user.email)

        return VerificationTokenPrincipalDto(UserDto.SimpleResponse(user), token)
    }

    private fun verifyJWT(email: String, jwt: String, userId: String? = null): JoinAttempt {
        if (!jwtTokenProvider.validateToken(jwt))
            throw JWTInvalidException("JWT is invalid")
        jwtTokenProvider.getEmailFromJwt(jwt)
            .let {
                if (it != email)
                    throw JWTInvalidException("JWT does not correspond to the email")
            }
        val attempt = joinAttemptRepository.findByEmail(email)!!
            .also {
                if (!passwordEncoder.matches(jwt, it.jwt))
                    throw JWTInvalidException("JWT does not correspond to any sign up request")
            }
            .also { joinAttemptRepository.deleteById(it.id) }
        userId?.let {
            if (userRepository.existsByUserId(userId))
                throw UserIdAlreadyExistException("User with this id already exists")
        }
        return attempt
    }

    private fun generateJoinJWT(email: String): String {
        val jwt = jwtTokenProvider.generateToken(email, join = true)
        val existingAttempt = joinAttemptRepository.findByEmail(email)

        existingAttempt?.let {
            it.jwt = passwordEncoder.encode(jwt)
            joinAttemptRepository.save(it)
        } ?: run {
            joinAttemptRepository.save(JoinAttempt(email = email, jwt = passwordEncoder.encode(jwt)))
        }

        return jwt
    }

    private fun generateVerificationToken(email: String): String {
        val token = jwtTokenProvider.generateToken(email, join = false)
        val existingToken = verificationTokenRepository.findByEmail(email)

        existingToken?.let {
            it.token = passwordEncoder.encode(token)
            verificationTokenRepository.save(it)
        } ?: run {
            val verificationToken = VerificationToken(email, passwordEncoder.encode(token))
            verificationTokenRepository.save(verificationToken)
        }

        return token
    }
}
