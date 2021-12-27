package com.wafflestudio.waflog.domain.ping.api

import com.wafflestudio.waflog.domain.ping.dto.PingDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PingController {

    @GetMapping("/ping/")
    fun getPong(): PingDto.Response {
        return PingDto.Response();
    }
}
