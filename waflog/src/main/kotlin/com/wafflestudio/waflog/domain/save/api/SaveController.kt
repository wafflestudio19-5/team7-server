package com.wafflestudio.waflog.domain.save.api

import com.wafflestudio.waflog.domain.save.dto.SaveDto
import com.wafflestudio.waflog.domain.save.service.SaveService
import com.wafflestudio.waflog.domain.user.model.User
import com.wafflestudio.waflog.global.auth.CurrentUser
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/save")
class SaveController(
    private val saveService: SaveService
) {
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun writeSave(@RequestBody createRequest: SaveDto.CreateRequest, @CurrentUser user: User) {
        saveService.writeSave(createRequest, user)
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    fun putSave(@RequestParam("id") token: String, @CurrentUser user: User): SaveDto.Response {
        return SaveDto.Response(saveService.getSave(token, user))
    }
}
