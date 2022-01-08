package com.wafflestudio.waflog.domain.image.service

import com.wafflestudio.waflog.domain.image.S3Utils
import com.wafflestudio.waflog.domain.image.exception.ImageNotFoundException
import com.wafflestudio.waflog.domain.image.repository.ImageRepository
import com.wafflestudio.waflog.domain.user.model.User
import com.wafflestudio.waflog.global.auth.CurrentUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class ImageService(
    private val imageRepository: ImageRepository
) {
    @Autowired
    lateinit var s3Utils: S3Utils

    fun uploadImage(image: MultipartFile, @CurrentUser user: User): String {
        val folderName = user.email.split("@").first()
        return s3Utils.uploadTo(image, folderName)
    }

    fun removeImage(token: String, @CurrentUser user: User) {
        val image = imageRepository.findByEmailAndToken(user.email, token)
            ?: throw ImageNotFoundException("image not found")
        s3Utils.remove(user.email, token, image.originalName)
    }
}
