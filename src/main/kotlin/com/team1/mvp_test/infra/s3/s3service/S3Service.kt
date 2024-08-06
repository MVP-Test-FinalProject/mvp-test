package com.team1.mvp_test.infra.s3.s3service

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.DeleteObjectRequest
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.util.IOUtils
import com.team1.mvp_test.common.error.S3ErrorMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.util.*


@Service
class S3Service(
    private val amazonS3: AmazonS3,
    @Value("\${cloud.aws.s3.bucket}") val bucket: String,
    @Value("\${cloud.aws.s3.baseurl}") val s3BaseUrl: String,
) {

    fun uploadMvpTestFile(file: MultipartFile): String {
        val dir = "mvpTestImage/"
        val allowedExtensions = arrayOf("jpg", "jpeg", "png")
        return upload(file, dir, allowedExtensions)
    }

    fun uploadStepFile(file: MultipartFile): String {
        val dir = "stepDocument/"
        val allowedExtensions = arrayOf("ppt", "pptx", "pdf")
        return upload(file, dir, allowedExtensions)
    }

    fun uploadReportFile(files: MutableList<MultipartFile>): List<String> {
        val dir = "reportDocument/"
        val allowedExtensions = arrayOf("jpg", "jpeg", "png")
        return files.map { file -> upload(file, dir, allowedExtensions) }

    }

    private fun upload(file: MultipartFile, dir: String, allowedExtensions: Array<String>): String {
        val maxFileSize = 10 * 1024 * 1024

        if (file.size > maxFileSize) throw IllegalArgumentException(S3ErrorMessage.EXCEED_FILE_SIZE.message)

        val extension = file.originalFilename?.let { validateFileExtension(it, allowedExtensions) }
            ?: throw IllegalArgumentException(S3ErrorMessage.FILE_TYPE_NOT_VALID.message)

        val fileName = UUID.randomUUID().toString() + "-" + file.originalFilename!!

        val bytes = IOUtils.toByteArray(file.inputStream)

        val objMeta = ObjectMetadata().apply {
            contentType = getContentType(extension)
            contentLength = bytes.size.toLong()
        }

        val byteArrayIs = ByteArrayInputStream(bytes)
        val s3Key = "$dir$fileName"

        amazonS3.putObject(bucket, s3Key, byteArrayIs, objMeta)

        return amazonS3.getUrl(bucket, s3Key).toString()
    }

    private fun validateFileExtension(fileName: String, allowedExtensions: Array<String>): String {
        val extensionIndex = fileName.lastIndexOf('.')

        if (extensionIndex == -1) throw IllegalArgumentException(S3ErrorMessage.FILE_TYPE_NOT_VALID.message)

        val extension = fileName.substring(extensionIndex + 1).lowercase(Locale.getDefault())

        if (!allowedExtensions.contains(extension)) throw IllegalArgumentException(S3ErrorMessage.FILE_TYPE_NOT_VALID.message)

        return extension
    }

    private fun getContentType(extension: String): String {
        return when (extension) {
            "jpg" -> "jpg"
            "jpeg" -> "jpeg"
            "png" -> "png"
            "ppt" -> "ppt"
            "pptx" -> "pptx"
            "pdf" -> "pdf"
            else -> "application/octet-stream"
        }
    }

    fun deleteFile(fileUrl: String) {
        val baseUrl = s3BaseUrl
        val filePath = URLDecoder.decode(fileUrl.removePrefix(baseUrl), StandardCharsets.UTF_8.name())
        amazonS3.deleteObject(DeleteObjectRequest(bucket, filePath))
    }

    fun deleteReportFiles(fileUrls: List<String>?) {
        val baseUrl = s3BaseUrl
        fileUrls?.forEach { fileUrl ->
            val filePath = URLDecoder.decode(fileUrl.removePrefix(baseUrl), StandardCharsets.UTF_8.name())
            amazonS3.deleteObject(DeleteObjectRequest(bucket, filePath))
        }
    }
}

