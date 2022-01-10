package com.wafflestudio.waflog.domain.image.service

import com.wafflestudio.waflog.domain.image.dto.ImageDto
import com.wafflestudio.waflog.domain.image.exception.ImageNotFoundException
import com.wafflestudio.waflog.domain.image.exception.InvalidImageFormException
import com.wafflestudio.waflog.domain.image.model.Image
import com.wafflestudio.waflog.domain.image.repository.ImageRepository
import com.wafflestudio.waflog.domain.user.model.User
import com.wafflestudio.waflog.global.auth.CurrentUser
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class ImageService(
    private val imageRepository: ImageRepository,
    private val s3Service: S3Service
) {

    fun uploadImage(image: MultipartFile, @CurrentUser user: User): String {
        val fileToken = UUID.randomUUID().toString()
        val fileName = image.originalFilename!!
        if (listOf("jpg", "JPG", "jpeg", "JPEG", "gif", "GIF").none { it == fileName.split(".").last() })
            throw InvalidImageFormException("this format is not allowed to upload")
        val uploadImage = Image(user.email, fileToken, fileName)
        val folderName = user.email.split("@").first()
        imageRepository.save(uploadImage)
        return s3Service.uploadTo(image, folderName, fileToken, fileName)
    }

    fun removeImage(removeRequest: ImageDto.RemoveRequest, @CurrentUser user: User) {
        val fileToken = removeRequest.token
        val image = imageRepository.findByEmailAndToken(user.email, fileToken)
            ?: throw ImageNotFoundException("image not found")
        val folderName =user.email.split("@").first()
        imageRepository.deleteById(image.id)
        s3Service.remove(folderName, fileToken, image.originalName)
    }
}
