package com.wafflestudio.waflog

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class WaflogApplication

fun main(args: Array<String>) {
    runApplication<WaflogApplication>(*args)
}
