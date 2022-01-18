package com.wafflestudio.waflog.domain.image.service

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.wafflestudio.waflog.domain.image.exception.ImageNotUploadedException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class S3Service(
    private var amazonS3: AmazonS3
) {
    @Value("\${cloud.aws.s3.bucket}")
    lateinit var bucketName: String

    fun uploadTo(file: MultipartFile, folderName: String, fileToken: String, fileName: String): Pair<String, String> {
        val keyName = "images/$folderName/$fileToken/$fileName"
        val inputStream = file.inputStream
        val contentType = file.contentType
        val meta = ObjectMetadata()
        meta.contentLength = inputStream.available().toLong()
        meta.contentType = contentType

        val putObjectRequest = PutObjectRequest(bucketName, keyName, inputStream, meta)
        amazonS3.putObject(putObjectRequest)
            ?.let { return Pair(fileToken, "https://image-waflog.kro.kr/$keyName") }
            ?: throw ImageNotUploadedException("image is not uploaded since S3 Server error occurred")
    }

    fun remove(folderName: String, fileToken: String, fileName: String) {
        val keyName = "images/$folderName/$fileToken/$fileName"
        amazonS3.deleteObject(bucketName, keyName)
    }
}
