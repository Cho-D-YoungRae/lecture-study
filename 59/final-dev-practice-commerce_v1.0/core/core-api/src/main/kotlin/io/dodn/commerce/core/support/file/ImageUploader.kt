package io.dodn.commerce.core.support.file

import io.dodn.commerce.core.domain.User
import io.dodn.commerce.storage.db.core.ImageEntity
import io.dodn.commerce.storage.db.core.ImageRepository
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@Component
class ImageUploader(
    private val imageRepository: ImageRepository,
) {
    fun uploadImage(user: User, file: MultipartFile): UploadResult {
        // Mock S3 업로드 - 실제 S3 업로드 대신 Mock URL 생성
        val mockS3Url = uploadToS3Mock(file)

        // ImageEntity 저장
        val imageEntity = ImageEntity(
            userId = user.id,
            imageUrl = mockS3Url,
            originalFilename = file.originalFilename ?: "unknown",
        )
        val savedEntity = imageRepository.save(imageEntity)

        return UploadResult(
            id = savedEntity.id,
            imageUrl = savedEntity.imageUrl,
        )
    }

    private fun uploadToS3Mock(file: MultipartFile): String {
        // Mock S3 업로드 구현
        // 실제로는 S3 클라이언트를 사용하여 업로드하지만, 여기서는 Mock URL 생성
        val uniqueId = UUID.randomUUID().toString()
        val filename = file.originalFilename ?: "unknown"
        return "https://mock-s3-bucket.s3.amazonaws.com/images/${uniqueId}_$filename"
    }
}
