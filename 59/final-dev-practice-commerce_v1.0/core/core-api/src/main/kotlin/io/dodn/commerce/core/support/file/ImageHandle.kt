package io.dodn.commerce.core.support.file

data class ImageHandle(
    val addImageIds: List<Long>,
    val deleteImageIds: List<Long>,
) {
    fun hasImagesToAdd(): Boolean {
        return addImageIds.isNotEmpty()
    }

    fun hasImagesToDelete(): Boolean {
        return deleteImageIds.isNotEmpty()
    }
}
