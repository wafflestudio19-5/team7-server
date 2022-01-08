package com.wafflestudio.waflog.domain.image

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.util.*

@Configuration
class AWSConfiguration {
    @Bean
    fun assetS3Client(
        @Value("\${cloud.aws.credentials.access-key}")
        accessKey: String,
        @Value("\${cloud.aws.credentials.secret-key}")
        secretKey: String

    ): AmazonS3 {
        return AmazonS3ClientBuilder
            .standard()
            .withCredentials(AWSStaticCredentialsProvider(BasicAWSCredentials(accessKey, secretKey)))
            .withRegion(Regions.AP_NORTHEAST_2)
            .build()
    }
}

@Component
class S3Utils {
    @Value("\${cloud.aws.s3.bucket}")
    lateinit var bucketName: String

    @Autowired
    lateinit var amazonS3: AmazonS3

    fun uploadTo(file: MultipartFile, folderName: String): String {
        val fileName = file.originalFilename!!
        val fileToken = UUID.randomUUID().toString()
        val keyName = "images/$folderName/$fileToken/$fileName"
        val uploadFile = File(System.getProperty("user.dir") + "/" + file.originalFilename!!)
        file.transferTo(uploadFile)
        amazonS3.putObject(bucketName, keyName, uploadFile)
        return "https://image-waflog.kro.kr/$keyName"
    }

    fun remove(folderName: String, fileToken: String, fileName: String) {
        val keyName = "images/$folderName/$fileToken/$fileName"
        amazonS3.deleteObject(bucketName, keyName)
    }
}
