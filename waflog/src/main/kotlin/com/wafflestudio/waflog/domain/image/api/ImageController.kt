package com.wafflestudio.waflog.domain.image.api

import com.wafflestudio.waflog.domain.user.model.User
import com.wafflestudio.waflog.global.auth.CurrentUser
import com.wafflestudio.waflog.domain.image.dto.ImageDto
import com.wafflestudio.waflog.domain.image.service.ImageService

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/image")
class ImageController(
    private val imageService: ImageService
) {
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun uploadImage(@RequestPart image: MultipartFile, @CurrentUser user: User): ImageDto.S3URL {
        return ImageDto.S3URL(imageService.uploadImage(image, user))
    }

    @DeleteMapping("")
    @ResponseStatus(HttpStatus.OK)
    fun removeImage(@RequestBody removeRequest: ImageDto.RemoveRequest, @CurrentUser user: User) {
        imageService.removeImage(removeRequest, user)
    }
}
