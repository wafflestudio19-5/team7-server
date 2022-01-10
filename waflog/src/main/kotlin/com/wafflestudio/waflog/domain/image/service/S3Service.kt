package com.wafflestudio.waflog.domain.image.service

import com.amazonaws.services.s3.AmazonS3
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File

@Service
class S3Service(
    private var amazonS3: AmazonS3
) {
    @Value("\${cloud.aws.s3.bucket}")
    lateinit var bucketName: String

    fun uploadTo(file: MultipartFile, folderName: String, fileToken: String, fileName: String): String {
        val keyName = "images/$folderName/$fileToken/$fileName"
        val uploadFile = File(System.getProperty("user.dir") + "/" + fileName)
        file.transferTo(uploadFile)
        amazonS3.putObject(bucketName, keyName, uploadFile)
        return "https://image-waflog.kro.kr/$keyName"
    }

    fun remove(folderName: String, fileToken: String, fileName: String) {
        val keyName = "images/$folderName/$fileToken/$fileName"
        amazonS3.deleteObject(bucketName, keyName)
    }
}
