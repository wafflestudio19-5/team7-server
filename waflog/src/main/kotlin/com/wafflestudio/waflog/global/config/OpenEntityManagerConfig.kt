package com.wafflestudio.waflog.global.config

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter
import org.springframework.stereotype.Component

@Component
@Configuration
class OpenEntityManagerConfig {
    @Bean
    fun openEntityManagerInViewFilter(): FilterRegistrationBean<OpenEntityManagerInViewFilter> {
        val filterRegistrationBean = FilterRegistrationBean<OpenEntityManagerInViewFilter>()
        filterRegistrationBean.filter = OpenEntityManagerInViewFilter()
        filterRegistrationBean.order = Int.MIN_VALUE
        return filterRegistrationBean
    }
}
