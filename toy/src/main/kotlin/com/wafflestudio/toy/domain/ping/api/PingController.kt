package com.wafflestudio.toy.domain.ping.api

import com.wafflestudio.toy.domain.ping.dto.PingDto
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin("https://localhost:3000")
@RestController
class PingController {
    @GetMapping("/ping/")
    fun getPong(): PingDto.Response {
        return PingDto.Response();
    }
}