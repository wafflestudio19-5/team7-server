package com.wafflestudio.waflog.global.oauth2.service

import com.wafflestudio.waflog.domain.user.model.User
import com.wafflestudio.waflog.domain.user.repository.UserRepository
import com.wafflestudio.waflog.global.auth.model.VerificationToken
import com.wafflestudio.waflog.global.oauth2.repository.OAuth2UserTokenRepository
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.stereotype.Service
import java.util.*
import javax.servlet.http.HttpSession

@Service
class CustomOidcUserService(
    private val userRepository: UserRepository,
    private val httpSession: HttpSession,
    private val oAuth2UserTokenRepository: OAuth2UserTokenRepository
) : OidcUserService() {
    override fun loadUser(userRequest: OidcUserRequest?): OidcUser {
        var oidcUser: OidcUser = super.loadUser(userRequest)
        var email: String = ""
        if (userRequest?.clientRegistration?.registrationId == "google") {
            email = saveGoogleUser(oidcUser, userRequest)
        }
        val token: String = generateSignInVerificationToken(email)
        httpSession.setAttribute("token", token)
        return oidcUser
    }

    fun saveIfAbsent(user: User) {
        var userByEmail: User? = userRepository.findByEmail(user.email)
        if (userByEmail == null) {
            userRepository.save(user)
        }
    }

    fun saveGoogleUser(oidcUser: OidcUser, userRequest: OidcUserRequest?): String {
        val user: User = User(
            email = oidcUser.email,
            userId = oidcUser.attributes["name"] as String,
            name = oidcUser.attributes["name"] as String,
            image = oidcUser.picture,
            pageTitle = oidcUser.attributes["name"] as String + ".log"
        )
        saveIfAbsent(user)
        return user.email
    }

    private fun generateSignInVerificationToken(email: String): String {
        val token = UUID.randomUUID().toString()
        val verificationToken = VerificationToken(email, token)
        oAuth2UserTokenRepository.save(verificationToken)
        return token
    }
}
