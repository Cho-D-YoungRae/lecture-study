package io.dodn.commerce.core.api.controller.v1

import io.dodn.commerce.core.domain.User
import io.dodn.commerce.core.support.file.ImageUploader
import io.dodn.commerce.core.support.file.UploadResult
import io.dodn.commerce.core.support.response.ApiResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class ImageController(
    private val imageUploader: ImageUploader,
) {
    @PostMapping("/v1/images/upload")
    fun uploadImage(user: User, @RequestParam file: MultipartFile): ApiResponse<UploadResult> {
        val uploadedImage = imageUploader.uploadImage(user, file)
        return ApiResponse.success(uploadedImage)
    }
}
