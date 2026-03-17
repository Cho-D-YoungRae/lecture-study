package io.dodn.commerce.storage.db.core

import io.dodn.commerce.core.enums.FavoriteTargetType
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import java.time.LocalDateTime

/**
 * 기존에는 productId 박혀있음 -> 셀러, 브랜드 찜 대응 어려움, 전략 필요
 *
 * - favorite entity 를 그대로 두고 브랜드 favorite entity 등 각각 찜을 만들거나
 * - favorite entity 를 그대로 두고 브랜드, 셀러를 대응할 수 있는 merchant favorite entity 를 만들거나
 * - favorite entity 를 쓰려면 마이그레이션 필요
 * - 아니면 그냥 productId 에 브랜드나 셀러 아이디 넣고 type 칼럼만 추가할 수도 있음 >> 칼럼명이 productId 인 것은 좋지는 않음
 *   - 칼럼 아이디는 변경하지 않고 어플리케이션 필드명만 변경해서 사용하는 것을 말할 수도 있음 > 하지만 db 도 자원이므로 잘 관리되는 것이 좋음
 *
 * > 마이그레이션을 하는 방향으로
 *
 * 작업 순서
 * 1. type, targetId 칼럼을 nullable 로 생성 > 데이터가 없을 것이므로 우선 nullable 로 생성해야 함
 * 2. targetId=productId, type = PRODUCT 마이그레이션
 * 3. 칼럼 not null 변경 및 productId 제거
 *
 * > targetId, type 를 디폴트 값을 주는 것으로 할 수는 있음
 *
 * ```kotlin
 * @Entity
 * @Table(name = "favorite")
 * class FavoriteEntity(
 *     val userId: Long,
 *     val productId: Long,
 *     favoritedAt: LocalDateTime,
 * ) : BaseEntity() {}
 * ```
 *
 * 여기서 그러면 처음부터 productId 보다 targetId 라는 보편적인 용어를 쓰는 것이 좋은 건가?
 *
 * > 그렇지 않다. 소프트웨어가 어떻게 발전할지 모른다. 변경이 발생할 것을 생각하면 끝도 없어진다.
 *
 * 마이그레이션을 하게 되면 단계별로 배포가 필요할 수 있다.
 * > productId 를 바로 삭제하지 않고 targetType, targetId 추가 후 마이그레이션 완료 후 productId 제거한다거나
 */
@Entity
@Table(name = "favorite")
class FavoriteEntity(
    val userId: Long,
    @Enumerated(EnumType.STRING)
    val targetType: FavoriteTargetType,
    val targetId: Long,
    favoritedAt: LocalDateTime,
) : BaseEntity() {
    var favoritedAt: LocalDateTime = favoritedAt
        protected set

    fun favorite() {
        active()
        favoritedAt = LocalDateTime.now()
    }
}
