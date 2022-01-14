package com.wafflestudio.waflog.global.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.wafflestudio.waflog.global.auth.JwtAuthenticationEntryPoint
import com.wafflestudio.waflog.global.auth.JwtAuthenticationFilter
import com.wafflestudio.waflog.global.auth.JwtTokenProvider
import com.wafflestudio.waflog.global.auth.SignInAuthenticationFilter
import com.wafflestudio.waflog.global.auth.service.VerificationTokenPrincipalDetailService
import com.wafflestudio.waflog.global.oauth2.OAuth2SuccessHandler
import com.wafflestudio.waflog.global.oauth2.service.CustomAuth2UserService
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.CorsUtils
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
    private val jwtTokenProvider: JwtTokenProvider,
    private val oAuth2SuccessHandler: OAuth2SuccessHandler,
    private val customAuth2UserService: CustomAuth2UserService,
    private val userPrincipalDetailService: VerificationTokenPrincipalDetailService,
    private val objectMapper: ObjectMapper,
    private val passwordEncoder: PasswordEncoder
) : WebSecurityConfigurerAdapter() {
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(daoAuthenticationProvider())
    }

    @Bean
    fun daoAuthenticationProvider(): DaoAuthenticationProvider {
        val provider = DaoAuthenticationProvider()
        provider.setPasswordEncoder(passwordEncoder)
        provider.setUserDetailsService(userPrincipalDetailService)
        return provider
    }

    override fun configure(http: HttpSecurity) {
        http
            .cors().configurationSource(corsConfigurationSource())
            .and()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .and()
            .addFilter(
                SignInAuthenticationFilter(
                    authenticationManager(), jwtTokenProvider, objectMapper
                )
            )
            .addFilter(JwtAuthenticationFilter(authenticationManager(), jwtTokenProvider))
            .authorizeRequests()
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
            .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
            .antMatchers(HttpMethod.GET, "/ping").permitAll()
            .antMatchers(
                HttpMethod.POST, "/api/v1/auth/user", "/api/v1/auth/user/login", "/api/v1/auth/user/info"
            ).permitAll()
            .antMatchers(HttpMethod.GET, "/api/v1/auth/verify").permitAll()
            .antMatchers(HttpMethod.POST, "/api/v1/auth/verify/login").permitAll()
            .antMatchers(HttpMethod.GET, "/api/v1/post/recent", "/api/v1/post/trend").permitAll()
            .antMatchers(HttpMethod.GET, "/api/v1/post/{\\d+}", "/api/v1/post/search").permitAll()
            .antMatchers(HttpMethod.GET, "/api/v1/post/@**/**").permitAll()
            .antMatchers(HttpMethod.GET, "/api/v1/post/{\\d+}/like/current").permitAll()
            .antMatchers(HttpMethod.GET, "/api/v1/user/@**", "/api/v1/user/@**/search").permitAll()
            .antMatchers(HttpMethod.GET, "/api/v1/user/@**/about", "/api/v1/user/@**/series").permitAll()
            .antMatchers(HttpMethod.GET, "/").permitAll()
            .anyRequest().authenticated()
            .and()
            .oauth2Login()
            .successHandler(oAuth2SuccessHandler)
            .userInfoEndpoint().userService(customAuth2UserService)
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val corsConfiguration = CorsConfiguration()
        corsConfiguration.allowCredentials = true
        corsConfiguration.addAllowedOrigin("https://waflog-web.kro.kr")
        corsConfiguration.addAllowedOrigin("http://waflog-local.kro.kr")
        corsConfiguration.addAllowedHeader("*")
        corsConfiguration.addAllowedMethod("GET")
        corsConfiguration.addAllowedMethod("POST")
        corsConfiguration.addAllowedMethod("DELETE")
        corsConfiguration.addAllowedMethod("PUT")
        corsConfiguration.addExposedHeader("Authentication")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", corsConfiguration)
        return source
    }
}
