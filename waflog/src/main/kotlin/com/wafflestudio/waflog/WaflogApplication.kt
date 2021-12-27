package com.wafflestudio.waflog

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class ToyApplication

fun main(args: Array<String>) {
    runApplication<ToyApplication>(*args)
}
