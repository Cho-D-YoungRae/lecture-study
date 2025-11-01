package io.dodn.commerce.core.domain

import io.dodn.commerce.core.support.OffsetLimit
import io.dodn.commerce.core.support.Page
import org.springframework.stereotype.Service

/**
 * 여러 가지 구현 패턴을 사용함 - 아래와 같이 되어있거나, 코드가 다 풀어져 있거나
 * 아래 형태가 강사가 가장 선호
 * - 서비스 안에는 비즈니스적인 플로우를 보여주고 싶음
 * - 그 안에는 행위 기준으로 이름이 지어진 컴포넌트들이 사용됨
 */
@Service
class ProductService(
    private val productFinder: ProductFinder,
) {
    fun findProducts(categoryId: Long, offsetLimit: OffsetLimit): Page<Product> {
        return productFinder.findByCategory(categoryId, offsetLimit)
    }

    fun findProduct(productId: Long): Product {
        return productFinder.find(productId)
    }
}
