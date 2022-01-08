package com.wafflestudio.waflog.domain.image.service

import com.wafflestudio.waflog.domain.image.exception.ImageNotFoundException
import com.wafflestudio.waflog.domain.image.model.Image
import com.wafflestudio.waflog.domain.image.repository.ImageRepository
import com.wafflestudio.waflog.domain.user.model.User
import com.wafflestudio.waflog.global.auth.CurrentUser
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class ImageService(
    private val imageRepository: ImageRepository,
    private val s3Utils: S3Service
) {

    fun uploadImage(image: MultipartFile, @CurrentUser user: User): String {
        val fileToken = UUID.randomUUID().toString()
        val fileName = image.originalFilename!!
        val uploadImage = Image(user.email, fileToken, fileName)
        val folderName = user.email.split("@").first()
        imageRepository.save(uploadImage)
        return s3Utils.uploadTo(image, folderName, fileToken, fileName)
    }

    fun removeImage(token: String, @CurrentUser user: User) {
        val image = imageRepository.findByEmailAndToken(user.email, token)
            ?: throw ImageNotFoundException("image not found")
        imageRepository.deleteById(image.id)
        s3Utils.remove(user.email, token, image.originalName)
    }
}
