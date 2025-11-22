package io.dodn.commerce.core.domain

data class Answer(
    val id: Long,
    val adminId: Long,
    val content: String,
) {
    /**
     * id = -1 이면 답변이 없는 상태를 의미한다고 협의한 상태.
     */
    companion object {
        val EMPTY: Answer = Answer(-1, -1, "")
    }
}
