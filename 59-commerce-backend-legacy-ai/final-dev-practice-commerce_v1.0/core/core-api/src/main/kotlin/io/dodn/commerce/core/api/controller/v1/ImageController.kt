package io.dodn.commerce.core.api.controller.v1

import io.dodn.commerce.core.domain.User
import io.dodn.commerce.core.support.file.ImageUploader
import io.dodn.commerce.core.support.file.UploadResult
import io.dodn.commerce.core.support.response.ApiResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

/**
 * ImageUploader 를 프레젠테이션 레이어에서 바로 쓰고 있어서 가이드라인에 위배됨
 * > import 를 보면 ImageUploader 는 support 쪽
 * > 이미지를 업로드하는 것은 도메인 기능은 아니어서 domain이 아닌 support 패키지에 구현함
 */
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
