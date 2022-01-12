package com.wafflestudio.waflog.global.auth

import org.springframework.security.core.annotation.AuthenticationPrincipal

@Target(AnnotationTarget.VALUE_PARAMETER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : user")
annotation class CurrentUser(val require: Boolean = true)
