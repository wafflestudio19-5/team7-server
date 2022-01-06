package com.wafflestudio.waflog.global.oauth2.service

import com.wafflestudio.waflog.domain.user.model.User
import com.wafflestudio.waflog.domain.user.repository.UserRepository
import com.wafflestudio.waflog.global.oauth2.model.OAuth2VerificationToken
import com.wafflestudio.waflog.global.oauth2.repository.OAuth2UserTokenRepository
import org.springframework.http.*
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponents
import org.springframework.web.util.UriComponentsBuilder
import java.util.*
import javax.servlet.http.HttpSession
import kotlin.collections.LinkedHashMap

@Service
class CustomAuth2UserService(
    private val userRepository: UserRepository,
    private val httpSession: HttpSession,
    private val oAuth2UserTokenRepository: OAuth2UserTokenRepository
) : DefaultOAuth2UserService() {
    override fun loadUser(userRequest: OAuth2UserRequest?): OAuth2User {
        var oauth2User: OAuth2User = super.loadUser(userRequest)
        var email: String = ""
        if (userRequest?.clientRegistration?.registrationId == "github") {
            email = saveGithubUser(oauth2User, userRequest)
        } else if (userRequest?.clientRegistration?.registrationId == "facebook") {
            email = saveFacebookUser(oauth2User, userRequest)
        }
        val token: String = generateSignInVerificationToken(email)
        httpSession.setAttribute("token", token)
        return oauth2User
    }

    fun saveIfAbsent(user: User) {
        var userByEmail: User? = userRepository.findByEmail(user.email)
        if (userByEmail == null) {
            userRepository.save(user)
        }
    }

    fun saveGithubUser(oauth2User: OAuth2User, userRequest: OAuth2UserRequest?): String {
        val factory = HttpComponentsClientHttpRequestFactory()
        val restTemplate = RestTemplate(factory)

        val header = HttpHeaders()
        header.set("Authorization", "bearer " + userRequest?.accessToken?.tokenValue)
        header.accept = Collections.singletonList(MediaType.APPLICATION_JSON)
        val entity = HttpEntity<Map<String, Any>>(header)
        val url = "https://api.github.com/user/emails"
        val uri: UriComponents = UriComponentsBuilder.fromHttpUrl(url).build()

        val result: ResponseEntity<List<*>> =
            restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, List::class.java)
        var mainEmailResponse: LinkedHashMap<*, *> = result.body?.filter {
            it as LinkedHashMap<*, *>
            it["primary"] == true
        }?.get(0) as LinkedHashMap<*, *>

        var email: String = mainEmailResponse["email"] as String
        val user: User = User(
            email = email,
            userId = oauth2User.attributes["login"] as String,
            name = oauth2User.attributes["login"] as String,
            image = oauth2User.attributes["avatar_url"] as String,
            pageTitle = oauth2User.attributes["login"] as String + ".log"
        )
        saveIfAbsent(user)
        return user.email
    }

    private fun saveFacebookUser(oauth2User: OAuth2User, userRequest: OAuth2UserRequest?): String {
        val factory = HttpComponentsClientHttpRequestFactory()
        val restTemplate = RestTemplate(factory)

        val header = HttpHeaders()
        header.set("Authorization", "bearer " + userRequest?.accessToken?.tokenValue)
        header.accept = Collections.singletonList(MediaType.APPLICATION_JSON)
        val entity = HttpEntity<Map<String, Any>>(header)
        val url = "https://graph.facebook.com/" + oauth2User.attributes["id"] + "/picture"
        val uri: UriComponents = UriComponentsBuilder.fromHttpUrl(url).build()

        val result: ResponseEntity<ByteArray> =
            restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, ByteArray::class.java)
        var imageResponse: ByteArray = result.body as ByteArray
        /*
        TODO: saving imageResponse(Bytearray) in S3 and return url
         */
        val user: User = User(
            email = oauth2User.attributes["email"] as String,
            userId = oauth2User.attributes["name"] as String,
            name = oauth2User.attributes["name"] as String,
            pageTitle = oauth2User.attributes["name"] as String + ".log"
        )
        saveIfAbsent(user)
        return user.email
    }

    private fun generateSignInVerificationToken(email: String): String {
        val token = UUID.randomUUID().toString()
        val oAuth2VerificationToken = OAuth2VerificationToken(email, token)
        oAuth2UserTokenRepository.save(oAuth2VerificationToken)
        return token
    }
}
