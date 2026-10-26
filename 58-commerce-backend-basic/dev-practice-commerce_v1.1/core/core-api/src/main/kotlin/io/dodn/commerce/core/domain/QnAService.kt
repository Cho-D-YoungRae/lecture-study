package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.core.support.OffsetLimit
import io.dodn.commerce.core.support.Page
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.storage.db.core.AnswerRepository
import io.dodn.commerce.storage.db.core.QuestionEntity
import io.dodn.commerce.storage.db.core.QuestionRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

/**
 * 여기서는 컴포넌트화를 시켜놓지 않음.
 * CRUD 가 간단한 경우 이와 같은 것도 괜찮을 수 있음.
 * 장기적으로 소프트웨어가 성장해나간다고 생각하면 이곳 저곳의 코드 형태가 너무 다양하면 새로운 사람들이 파악하기 어려움. -> 일관된 형태가 좋음.
 */
@Service
class QnAService(
    private val questionRepository: QuestionRepository,
    private val answerRepository: AnswerRepository,
) {
    fun findQnA(productId: Long, offsetLimit: OffsetLimit): Page<QnA> {
        val questions = questionRepository.findByProductIdAndStatus(
            productId,
            EntityStatus.ACTIVE,
            offsetLimit.toPageable(),
        )

        val answers = answerRepository.findByQuestionIdIn(questions.content.map { it.id })
            .filter { it.isActive() }
            .associateBy { it.questionId }

        return Page(
            questions.content.map {
                QnA(
                    question = Question(
                        id = it.id,
                        userId = it.userId,
                        title = it.title,
                        content = it.content,
                    ),
                    answer = answers[it.id]?.let { answer ->
                        Answer(answer.id, answer.adminId, answer.content)
                    } ?: Answer.EMPTY,
                )
            },
            questions.hasNext(),
        )
    }

    fun addQuestion(user: User, productId: Long, content: QuestionContent): Long {
        val saved = questionRepository.save(
            QuestionEntity(
                userId = user.id,
                productId = productId,
                title = content.title,
                content = content.content,
            ),
        )
        return saved.id
    }

    @Transactional
    fun updateQuestion(user: User, questionId: Long, content: QuestionContent): Long {
        val found = questionRepository.findByIdAndUserId(questionId, user.id)?.takeIf { it.isActive() } ?: throw CoreException(ErrorType.NOT_FOUND_DATA)
        found.updateContent(content.title, content.content)
        return found.id
    }

    @Transactional
    fun removeQuestion(user: User, questionId: Long): Long {
        val found = questionRepository.findByIdAndUserId(questionId, user.id)?.takeIf { it.isActive() } ?: throw CoreException(ErrorType.NOT_FOUND_DATA)
        found.delete()
        return found.id
    }

    /**
     * NOTE: 답변은어드민 쪽 기능임
     * fun addAnswer(user: User, questionId: Long, content: String): Long {...}
     * fun updateAnswer(user: User, answerId: Long, content: String): Long {...}
     * fun removeAnswer(user: User, answerId: Long): Long {...}
     */
}
