package com.wafflestudio.waflog.global.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
class SecurityConfig() : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http
            .cors().configurationSource(corsConfigurationSource())
            .and()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.GET, "/ping").permitAll() // SignUp user
            .antMatchers(HttpMethod.POST, "/api/v1/auth/user", "/api/v1/auth/user/info").permitAll()
            .antMatchers(HttpMethod.POST, "/api/v1/auth/verify").permitAll()
            .antMatchers(HttpMethod.GET, "/api/v1/post/recent", "/api/v1/post/trend").permitAll()
            .antMatchers(HttpMethod.GET, "/api/v1/post/{\\d+}", "/api/v1/post/search").permitAll()
            .anyRequest().authenticated() // Because signin api doesn't exist yet, so to test permit all request
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        var corsConfiguration = CorsConfiguration()
        corsConfiguration.addAllowedOrigin("*")
        corsConfiguration.addAllowedHeader("*")
        corsConfiguration.addAllowedMethod("*")
        var source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", corsConfiguration)
        return source
    }
}