dev-practice-commerce 프로젝트 가이드라인

본 문서는 신규 합류한 개발자가 빠르게 프로젝트에 적응하고 일관된 방식으로 코드를 작성할 수 있도록, 모듈/패키지 구조, 코드 스타일, 설계 스타일을 정리합니다.

- 빌드 시스템: Gradle (Kotlin DSL)
- 언어/런타임: Kotlin (JVM), Spring Boot
- 정적 분석/포맷팅: ktlint 적용

1. 모듈 구조 개요
프로젝트는 멀티 모듈로 구성되어 있으며, 각 모듈의 책임은 다음과 같습니다.

- core:core-api
  - 애플리케이션 계층과 프레젠테이션(Web API) 계층을 포함합니다.
  - 주요 내용: Spring MVC Controller, 요청/응답 DTO, 개념 서비스, 공용 응답/에러/인증 지원 코드.
  - 경로: core/core-api/src/main/kotlin/io/dodn/commerce/core/...

- core:core-enum
  - 시스템 전반에서 공통으로 사용하는 열거형 타입 모음 모듈입니다.
  - 경로: core/core-enum/src/main/kotlin/io/dodn/commerce/core/enums

- storage:db-core
  - 영속성 어댑터(JPA) 모듈입니다. 데이터베이스 설정, 엔티티, 컨버터 등을 포함합니다.
  - 주요 내용: config, converter, core 패키지의 JPA 엔티티(예: PaymentEntity).
  - 경로: storage/db-core/src/main/kotlin/io/dodn/commerce/storage/db/core/...

- support:logging
  - 로깅 관련 공통 설정 모듈입니다. logback 설정 리소스를 포함합니다.
  - 경로: support/logging/src/main/resources/logback

- support:monitoring
  - 모니터링/관측(Observability) 관련 공통 설정 모듈입니다. (Micrometer/프로메테우스 연계 등)
  - 경로: support/monitoring/src/main/resources

의존성 및 빌드 공통 설정:
- 루트 build.gradle.kts에서 모든 서브모듈에 Kotlin, Spring Boot, JPA, ktlint 플러그인을 적용합니다.
- bootJar는 비활성화하고 일반 jar를 생성합니다. 즉, 각 모듈은 라이브러리로 패키징됩니다.
- 테스트 태깅: unitTest, contextTest, developTest 태스크 제공 (JUnit5 Tag 기반)

2. 패키지 구조
기본 패키지 프리픽스는 io.dodn.commerce 입니다. 주요 하위 구조는 다음과 같습니다.

- core:core-api
  - io.dodn.commerce.core.api.config — Spring 전역 설정(예: WebMvc, Jackson, CORS 등)
  - io.dodn.commerce.core.api.controller
    - ...controller.v1 — 버전별 REST 컨트롤러 (예: CartController)
    - ...controller.v1.request — 요청 DTO (AddCartItemRequest, ModifyCartItemRequest 등)
    - ...controller.v1.response — 응답 DTO (CartResponse, CartItemResponse 등)
  - io.dodn.commerce.core.domain — 개념 서비스/객체 (CartService, User, PointService, ReviewService 등)
  - io.dodn.commerce.core.support
    - ...support.auth — 인증/사용자 주입 등
    - ...support.error — 예외 및 에러 응답 매핑
    - ...support.response — 공통 응답 래퍼(ApiResponse)

- core:core-enum
  - io.dodn.commerce.core.enums — 시스템 공용 Enum (예: PointType)

- storage:db-core
  - io.dodn.commerce.storage.db.core.config — DB/JPA 설정
  - io.dodn.commerce.storage.db.core.converter — JPA 컨버터
  - io.dodn.commerce.storage.db.core — JPA 엔티티 (예: PaymentEntity)

- support 모듈
  - 로깅/모니터링 관련 리소스 중심의 구성

패키징/네이밍 원칙:
- 패키지명: 모두 소문자, 점(.)으로 개념을 좁혀갑니다.
- API 버저닝: controller.v1 과 같이 패키지 및 URL 경로 모두에 버전을 명시합니다.
- DTO는 request, response 하위에 위치시키고, 개념 변환 메서드/팩토리를 제공합니다.

3. 코드 스타일 가이드
본 프로젝트는 Kotlin + Spring Boot 표준 컨벤션을 따르며, ktlint로 자동 검사합니다.

- 일반 규칙
  - 들여쓰기/줄바꿈/임포트 순서는 ktlint 기본 규칙 준수
  - 클래스/인터페이스/오브젝트/열거형: PascalCase
  - 함수/프로퍼티/변수: camelCase
  - 상수: UPPER_SNAKE_CASE
  - 파일당 public 클래스는 1개를 권장, 관련 private 타입은 동일 파일 내 중첩 또는 하위에 배치
  - Null 안정성: 가능한 non-null 선호, 필요 시 명시적 nullable과 안전 연산자 사용
  - 불변성 선호: 가능하면 val 사용, DTO/뷰 모델은 data class 권장

- Kotlin/Spring 특화 규칙
  - DI는 생성자 주입을 기본으로 합니다.
  - @RestController + HTTP 메서드 매핑 사용, 엔드포인트는 소문자, 하이픈(-) 사용, 복수 명사 사용 권장
    - 예) /v1/cart/items
  - 컨트롤러에서는 비즈니스 로직을 두지 않고, 입력 검증/매핑과 응답 변환만 담당합니다.
  - 응답은 공통 래퍼 ApiResponse<T>를 통해 감쌉니다. 성공/실패 규격은 support.response/support.error를 따릅니다.
  - 요청 DTO는 개념 입력 모델로 변환하는 toXxx()를 제공합니다.
  - 응답 DTO는 정적 팩토리 of(concept)/from(concept) 패턴을 권장합니다.

- 예외/에러 처리
  - 서버 내부 예외는 support.error의 글로벌 핸들러 정책을 따릅니다.
  - 비즈니스 예외는 의미 있는 개념 별 에러코드/메시지를 사용합니다.

- 테스트
  - JUnit5 + Spring Boot Test, 필요 시 @Tag("develop"), @Tag("context") 를 활용
  - 태스크: ./gradlew test, unitTest, contextTest, developTest
  - 단위 테스트에서 개념 로직을 우선 검증하고, 컨텍스트 로딩이 필요한 경우에만 통합 테스트 작성

4. 설계 스타일 가이드
상세한 설계 규칙 및 레이어 정책은 [.junie/guidelines.md](.junie/guidelines.md) 파일을 참조하세요. 본 섹션에는 핵심 요약만 기재합니다.

- 레이어 구조: Presentation, Business, Logic, Data Access의 4계층 분리.
- 참조 규칙: 순방향 참조만 허용하며, 하위 레이어를 건너뛰지 않는다.
- 트랜잭션: Logic Layer에만 `@Transactional`을 사용한다.
- 용어 통일: '도메인' 대신 '개념' 용어를 사용한다.

5. 명명 규칙 및 컨벤션
- 엔드포인트: 소문자-하이픈, 복수형 리소스명 사용 (/v1/cart/items)
- DTO: XxxRequest, XxxResponse
- 서비스: XxxService
- 엔티티: XxxEntity
- 컨버터: XxxConverter
- 열거형: 개념 의미가 드러나는 명사형 (PointType 등)

6. 예시 스니펫
- 컨트롤러 패턴 (발췌: CartController)

```kotlin
@RestController
class CartController(
    private val cartService: CartService,
) {
    @GetMapping("/v1/cart")
    fun getCart(user: User): ApiResponse<CartResponse> {
        val cart = cartService.getCart(user)
        return ApiResponse.success(CartResponse(cart.items.map { CartItemResponse.of(it) }))
    }

    @PostMapping("/v1/cart/items")
    fun addCartItem(user: User, @RequestBody request: AddCartItemRequest): ApiResponse<Any> {
        cartService.addCartItem(user, request.toAddCartItem())
        return ApiResponse.success()
    }
}
```

- Enum 위치: core/core-enum/src/main/kotlin/io/dodn/commerce/core/enums/PointType.kt
- 엔티티 위치: storage/db-core/src/main/kotlin/io/dodn/commerce/storage/db/core/PaymentEntity.kt

7. 작업 가이드(Feature 추가 절차)
- 공용 Enum이 필요하면 core-enum에 추가합니다.
- 개념 규칙/유스케이스는 core-api의 domain 패키지에 서비스로 추가합니다.
- API가 필요하면 controller.v1 하위에 컨트롤러/요청/응답 DTO를 추가하고, DTO ↔  개념 변환을 구현합니다.
- 저장소 연동이 필요하면 storage:db-core에 엔티티/리포지토리/컨버터를 정의하고, 개념과의 매핑을 분리합니다.
- 테스트는 단위 테스트를 우선하며, 필요한 경우 컨텍스트/통합 테스트를 보완합니다.

8. 빌드 & 테스트
- 전체 테스트: ./gradlew test
- 단위 테스트만: ./gradlew unitTest
- 컨텍스트 테스트만: ./gradlew contextTest
- 개발 태그 테스트: ./gradlew developTest

9. 품질 및 일관성
- 커밋 전 ktlint 자동 검사에 의해 스타일 위반이 검출될 수 있습니다. IDE에서 ktlint 플러그인/포맷팅을 사용하는 것을 권장합니다.
- 공통 응답 규격(ApiResponse)과 에러 핸들링 정책(support.error)을 준수하세요.
- API 버전 정책(v1 패키지/경로)을 반드시 지킵니다.

---
본 가이드는 코드와 설정을 바탕으로 작성되었으며, 실제 운영 정책/보안/인프라 세부 사항은 별도 문서를 참조할 수 있습니다. 변경 사항이 생길 경우, 본 문서를 함께 업데이트해 주세요.