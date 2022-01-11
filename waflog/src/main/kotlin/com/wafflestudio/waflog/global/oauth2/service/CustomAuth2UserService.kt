package com.wafflestudio.waflog.global.oauth2.service

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponents
import org.springframework.web.util.UriComponentsBuilder
import java.util.Collections

@Service
class CustomAuth2UserService : DefaultOAuth2UserService() {
    override fun loadUser(userRequest: OAuth2UserRequest?): OAuth2User {
        val oauth2User: OAuth2User = super.loadUser(userRequest)

        // get well-formatted user from different resource servers
        return when (userRequest?.clientRegistration?.registrationId) {
            "github" -> getGithubUser(oauth2User, userRequest)
            "facebook" -> getFacebookUser(oauth2User, userRequest)
            "google" -> getGoogleUser(oauth2User, userRequest)
            else -> oauth2User
        }
    }

    fun getGithubUser(oauth2User: OAuth2User, userRequest: OAuth2UserRequest?): OAuth2User {
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
        val mainEmailResponse: LinkedHashMap<*, *> = result.body?.filter {
            it as LinkedHashMap<*, *>
            it["primary"] == true
        }?.get(0) as LinkedHashMap<*, *>

        val email: String = mainEmailResponse["email"] as String
        val memberAttributes: HashMap<String, Any?> = hashMapOf(
            "email" to email,
            "image" to oauth2User.attributes["avatar_url"]
        )

        return DefaultOAuth2User(
            Collections.singleton(SimpleGrantedAuthority("ROLE_USER")),
            memberAttributes,
            "email"
        )
    }

    private fun getFacebookUser(oauth2User: OAuth2User, userRequest: OAuth2UserRequest?): OAuth2User {
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

        val memberAttributes: HashMap<String, Any?> = hashMapOf(
            "email" to oauth2User.attributes["email"]
        )

        return DefaultOAuth2User(
            Collections.singleton(
                SimpleGrantedAuthority("ROLE_USER")
            ),
            memberAttributes,
            "email"
        )
    }

    fun getGoogleUser(oauth2User: OAuth2User, userRequest: OAuth2UserRequest?): OAuth2User {

        val memberAttributes: HashMap<String, Any?> = hashMapOf(
            "email" to oauth2User.attributes["email"],
            "image" to oauth2User.attributes["picture"]
        )

        return DefaultOAuth2User(
            Collections.singleton(SimpleGrantedAuthority("ROLE_USER")),
            memberAttributes,
            "email"
        )
    }
}
