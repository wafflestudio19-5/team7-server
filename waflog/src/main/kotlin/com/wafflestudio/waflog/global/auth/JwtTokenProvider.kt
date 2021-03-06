package com.wafflestudio.waflog.global.auth

import com.wafflestudio.waflog.domain.user.repository.UserRepository
import com.wafflestudio.waflog.global.auth.exception.VerificationTokenNotFoundException
import com.wafflestudio.waflog.global.auth.model.AuthenticationToken
import com.wafflestudio.waflog.global.auth.model.VerificationTokenPrincipal
import com.wafflestudio.waflog.global.auth.repository.VerificationTokenRepository
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.SignatureException
import io.jsonwebtoken.UnsupportedJwtException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.Date

@Component
class JwtTokenProvider(
    private val userRepository: UserRepository,
    private val verificationTokenRepository: VerificationTokenRepository
) {
    private val logger = LoggerFactory.getLogger(JwtTokenProvider::class.java)
    val tokenPrefix = "Bearer "
    val headerString = "Authentication"

    @Value("\${app.jwt.jwt-secret-key}")
    private val jwtSecretKey: String? = null

    @Value("\${app.jwt.jwt-expiration-in-ms}")
    private val jwtExpirationInMs: Long? = null

    @Value("\${app.jwt.jwt-signup-expiration-in-ms}")
    private val jwtJoinExpirationInMs: Long? = null

    fun generateToken(email: String, join: Boolean = false): String {
        val claims: MutableMap<String, Any> = hashMapOf("email" to email)
        val now = Date()
        val expiryDate = Date(now.time + (if (!join) jwtExpirationInMs!! else jwtJoinExpirationInMs!!))
        return tokenPrefix + Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
            .compact()
    }

    fun generateToken(authentication: Authentication): String {
        val verificationTokenPrincipal = authentication.principal as VerificationTokenPrincipal
        return generateToken(verificationTokenPrincipal.verificationToken.email)
    }

    fun validateToken(authToken: String?): Boolean {
        if (authToken.isNullOrEmpty()) {
            logger.error("Token is not provided")
            return false
        }
        if (!authToken.startsWith(tokenPrefix)) {
            logger.error("Token not match type Bearer")
            return false
        }
        val authTokenWithoutPrefix = removePrefix(authToken)
        try {
            Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(authTokenWithoutPrefix)
            return true
        } catch (ex: SignatureException) {
            logger.error("Invalid JWT signature")
        } catch (ex: MalformedJwtException) {
            logger.error("Invalid JWT token")
        } catch (ex: ExpiredJwtException) {
            logger.error("Expired JWT token")
        } catch (ex: UnsupportedJwtException) {
            logger.error("Unsupported JWT token")
        } catch (ex: IllegalArgumentException) {
            logger.error("JWT claims string is empty.")
        }
        return false
    }

    fun removePrefix(tokenWithPrefix: String): String {
        return tokenWithPrefix.replace(tokenPrefix, "").trim { it <= ' ' }
    }

    fun getEmailFromJwt(token: String): String {
        var tokenWithoutPrefix = token
        tokenWithoutPrefix = removePrefix(tokenWithoutPrefix)
        val claims = Jwts.parser()
            .setSigningKey(jwtSecretKey)
            .parseClaimsJws(tokenWithoutPrefix)
            .body

        return claims.get("email", String::class.java)
    }

    fun getAuthenticationTokenFromJwt(token: String): Authentication {
        var tokenWithoutPrefix = token
        tokenWithoutPrefix = removePrefix(tokenWithoutPrefix)
        val claims = Jwts.parser()
            .setSigningKey(jwtSecretKey)
            .parseClaimsJws(tokenWithoutPrefix)
            .body
        // Recover User class from JWT
        val email = claims.get("email", String::class.java)
        val currentUser = userRepository.findByEmail(email)
        val currentAuthToken = verificationTokenRepository.findByEmail(email)
            ?: throw VerificationTokenNotFoundException("$email is not valid email, check token is expired")
        val userPrincipal = VerificationTokenPrincipal(currentUser, currentAuthToken)
        val authorises = userPrincipal.authorities
        // Make token with parsed data
        return AuthenticationToken(userPrincipal, null, authorises)
    }
}
