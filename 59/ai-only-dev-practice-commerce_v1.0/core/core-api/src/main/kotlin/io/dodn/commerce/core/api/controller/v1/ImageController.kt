package io.dodn.commerce.core.api.controller.v1

import io.dodn.commerce.core.support.response.ApiResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class ImageController {
    @PostMapping("/v1/images/upload")
    fun uploadImage(
        @RequestParam("file") file: MultipartFile,
    ): ApiResponse<String> {
        // 실제 구현에서는 파일을 스토리지에 업로드하고 URL을 반환해야 합니다.
        // 여기서는 간단히 Mock URL을 반환합니다.
        val imageUrl = "https://example.com/images/${System.currentTimeMillis()}_${file.originalFilename}"
        return ApiResponse.success(imageUrl)
    }
}
