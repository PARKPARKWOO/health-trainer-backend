package com.example.core.multimedia.adapter.out

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.example.core.multimedia.application.port.out.S3Port
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@Component
class S3Adapter(
    private val amazonS3: AmazonS3,
    @Value("\${cloud-front.domain}")
    private val cloudFrontDomain: String,
) : S3Port {
    override suspend fun uploadS3(files: List<MultipartFile>): List<String>? {
        return coroutineScope {
            files.map { file ->
                val metadata = ObjectMetadata().apply {
                    this.contentType = file.contentType
                    this.contentLength = file.size
                }
                async {
                    val fileName = "${UUID.randomUUID()}_${file.originalFilename}"
                    val putObjectRequest = PutObjectRequest(
                        "TODO BucketName",
                        fileName,
                        file.inputStream,
                        metadata,
                    )
                    amazonS3.putObject(putObjectRequest)
                    cloudFrontDomain + fileName
                }
            }
        }.awaitAll()
    }
}
