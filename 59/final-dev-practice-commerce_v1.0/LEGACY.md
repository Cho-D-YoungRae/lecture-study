# 레거시 코드 개선 항목

본 문서는 현재 프로젝트에서 발견된 일관성 없는 패턴, 규칙이 명확하지 않은 부분, 그리고 개선이 필요한 레거시 코드를 정리합니다.

---

## 1. 아키텍처 및 계층 분리 이슈

### 1.1 도메인 서비스의 인프라 계층 직접 의존
**현상:**
- `CartService`, `OrderService`, `PaymentService` 등 도메인 서비스가 `storage.db.core`의 엔티티와 리포지토리를 직접 의존
- 예: `CartService`가 `CartItemEntity`, `CartItemRepository`, `ProductRepository`를 직접 주입받아 사용

**문제점:**
- 도메인 계층이 인프라 세부사항에 강하게 결합됨
- 테스트 시 실제 JPA 리포지토리가 필요하여 단위 테스트 작성이 어려움
- 영속성 기술 변경 시 도메인 서비스 전체 수정 필요

**개선 방향:**
- 리포지토리 인터페이스를 도메인 계층에 정의 (Port)
- 인프라 계층에서 해당 인터페이스를 구현 (Adapter)
- 엔티티 ↔ 도메인 모델 변환을 인프라 계층에서 처리

**관련 파일:**
- `core/core-api/src/main/kotlin/io/dodn/commerce/core/domain/CartService.kt`
- `core/core-api/src/main/kotlin/io/dodn/commerce/core/domain/OrderService.kt`
- `core/core-api/src/main/kotlin/io/dodn/commerce/core/domain/PaymentService.kt`
- `core/core-api/src/main/kotlin/io/dodn/commerce/core/domain/PointService.kt`

### 1.2 컨트롤러의 과도한 책임
**현상:**
- 컨트롤러가 여러 도메인 서비스를 조합하여 복합 응답을 생성
- 예: `ProductController.findProduct()`가 `productService`, `productSectionService`, `reviewService`, `couponService` 4개의 서비스를 호출
- 예: `OrderController.findOrderForCheckout()`가 `orderService`, `ownedCouponService`, `pointService`를 조합

**문제점:**
- 컨트롤러가 서비스 조율(orchestration) 로직을 포함
- 프레젠테이션 계층이 비즈니스 로직 흐름을 알아야 함
- 동일한 조합 로직의 재사용이 어려움

**개선 방향:**
- Facade 패턴 또는 Use Case 계층 도입
- 복합 조회 로직을 별도 서비스로 분리
- 컨트롤러는 단일 서비스 호출과 DTO 변환에만 집중

**관련 파일:**
- `core/core-api/src/main/kotlin/io/dodn/commerce/core/api/controller/v1/ProductController.kt` (라인 36-46)
- `core/core-api/src/main/kotlin/io/dodn/commerce/core/api/controller/v1/OrderController.kt` (라인 62-71)

---

## 2. 서비스 계층 네이밍 불일치

### 2.1 서비스 클래스 네이밍 규칙 혼재
**현상:**
- `Service`, `Handler`, `Finder`, `Manager`, `Validator`, `Calculator`, `Loader` 등 다양한 네이밍 접미사 혼용
- 명확한 네이밍 규칙 없이 사용되고 있음

**예시:**
```
- CartService, OrderService, PaymentService (전통적 Service)
- PointHandler (Handler)
- ReviewFinder (Finder)
- ReviewManager (Manager)
- ReviewPolicyValidator (Validator)
- SettlementCalculator (Calculator)
- SettlementTargetLoader (Loader)
```

**문제점:**
- 각 접미사가 어떤 책임을 가지는지 명확하지 않음
- 새로운 컴포넌트 추가 시 네이밍 선택 기준이 모호함
- ReviewService는 내부적으로 Finder, Manager, Validator를 조합하지만, 다른 Service는 직접 구현

**개선 방향:**
- 각 접미사의 역할과 책임을 명확히 정의
  - `Service`: 유스케이스 조율
  - `Handler`: 특정 도메인 이벤트/작업 처리
  - `Finder`: 조회 전담
  - `Manager`: 생명주기 관리
  - `Validator`: 정책 검증
- 가이드라인 문서에 네이밍 규칙 추가
- 점진적으로 일관된 패턴으로 리팩토링

**관련 파일:**
- `core/core-api/src/main/kotlin/io/dodn/commerce/core/domain/` (전체)

---

## 3. 트랜잭션 관리 불일치

### 3.1 @Transactional 적용 기준 불명확
**현상:**
- 읽기 전용 메서드에 `@Transactional` 적용: `OrderService.getOrders()` (라인 58)
- 쓰기 작업인데 `@Transactional` 누락: `PaymentService.fail()` (라인 96)
- `ReviewService`의 모든 쓰기 메서드에 `@Transactional` 누락

**문제점:**
- 트랜잭션 경계가 명확하지 않아 데이터 일관성 문제 발생 가능
- 불필요한 트랜잭션으로 인한 성능 저하
- 트랜잭션 롤백이 필요한 상황에서 롤백되지 않을 수 있음

**개선 방향:**
- 명확한 트랜잭션 정책 수립
  - 읽기 전용: `@Transactional(readOnly = true)` 사용 또는 생략
  - 쓰기 작업: 반드시 `@Transactional` 적용
- 서비스 클래스 레벨에 기본 정책 적용 후 메서드별 오버라이드
- ReviewService의 쓰기 메서드들에 트랜잭션 추가

**관련 파일:**
- `core/core-api/src/main/kotlin/io/dodn/commerce/core/domain/OrderService.kt` (라인 58)
- `core/core-api/src/main/kotlin/io/dodn/commerce/core/domain/PaymentService.kt` (라인 96)
- `core/core-api/src/main/kotlin/io/dodn/commerce/core/domain/ReviewService.kt` (라인 23-39)

### 3.2 트랜잭션 경계와 도메인 이벤트 처리
**현상:**
- `ReviewService.addReview()`에서 리뷰 추가와 포인트 적립이 하나의 메서드에서 호출되지만 트랜잭션이 없음
- `PaymentService.success()`에서 여러 엔티티 변경과 포인트 처리가 하나의 트랜잭션에서 처리됨

**문제점:**
- 일부 작업만 성공하고 일부는 실패할 경우 데이터 불일치
- 도메인 이벤트와 트랜잭션 경계 관리가 명확하지 않음

**개선 방향:**
- 트랜잭션 경계를 명확히 정의
- 도메인 이벤트 발행 시점 정립 (트랜잭션 내 vs 커밋 후)
- 필요 시 분산 트랜잭션 또는 보상 트랜잭션 패턴 고려

---

## 4. 엔티티 ↔ 도메인 모델 변환 일관성 부족

### 4.1 변환 로직 위치 불일치
**현상:**
- `CartService.getCart()`: 서비스 내부에서 직접 엔티티를 도메인 객체로 변환 (라인 17-44)
- `PointService.balance()`, `histories()`: 서비스 내부에서 변환 (라인 14-34)
- `OrderService.create()`, `getOrder()`: 서비스 내부에서 변환

**문제점:**
- 엔티티 구조 변경 시 여러 서비스 수정 필요
- 변환 로직 중복
- 도메인 서비스가 영속성 계층의 세부사항을 알아야 함

**개선 방향:**
- 변환 책임을 인프라 계층(Adapter)으로 이동
- 도메인 모델에 정적 팩토리 메서드 추가: `Domain.from(entity)`
- 또는 별도의 Mapper 클래스 도입

**관련 파일:**
- `core/core-api/src/main/kotlin/io/dodn/commerce/core/domain/CartService.kt` (라인 17-44)
- `core/core-api/src/main/kotlin/io/dodn/commerce/core/domain/PointService.kt` (라인 14-34)
- `core/core-api/src/main/kotlin/io/dodn/commerce/core/domain/OrderService.kt` (라인 59-103)

---

## 5. DTO 변환 패턴 불일치

### 5.1 요청 DTO의 변환 메서드 네이밍
**현상:**
- `AddCartItemRequest.toAddCartItem()` - `to` 접두사 사용
- `ModifyCartItemRequest.toModifyCartItem(id)` - `to` 접두사 + 파라미터
- `CreateOrderRequest.toNewOrder(user)` - `to` 접두사 + 파라미터

**문제점:**
- 네이밍은 일관되지만, 일부는 파라미터를 받고 일부는 받지 않아 일관성 부족
- User를 파라미터로 받는 것이 적절한지 불명확 (User는 컨트롤러에서 주입받은 인증 정보)

**개선 방향:**
- 요청 DTO는 자체적으로 변환 가능한 정보만 포함하도록 설계
- 외부 컨텍스트(User 등)가 필요하면 서비스 레이어에서 조합
- 또는 정적 팩토리 메서드로 명확히 분리: `AddCartItem.from(request, userId)`

**관련 파일:**
- `core/core-api/src/main/kotlin/io/dodn/commerce/core/api/controller/v1/request/AddCartItemRequest.kt`
- `core/core-api/src/main/kotlin/io/dodn/commerce/core/api/controller/v1/request/ModifyCartItemRequest.kt`
- `core/core-api/src/main/kotlin/io/dodn/commerce/core/api/controller/v1/request/CreateOrderRequest.kt`

### 5.2 응답 DTO의 변환 메서드 네이밍
**현상:**
- `CartItemResponse.of(cartItem)` - 정적 팩토리 `of`
- `OrderCheckoutResponse.of(order, ownedCoupons, pointBalance)` - 정적 팩토리 `of` + 복수 파라미터
- `ProductResponse.of(result.content)` - 정적 팩토리 `of`

**문제점:**
- `of` 메서드가 단일 도메인 객체를 받을 때와 복수 파라미터를 받을 때가 혼재
- 복수 파라미터를 받는 경우 응답 DTO가 여러 도메인 지식을 알아야 함

**개선 방향:**
- 단일 도메인 변환: `of(domain)` 또는 `from(domain)`
- 복합 변환: 별도 정적 메서드 네이밍 (`create`, `compose` 등) 또는 서비스 레이어에서 조합
- 응답 DTO는 단순 데이터 전달에 집중

**관련 파일:**
- `core/core-api/src/main/kotlin/io/dodn/commerce/core/api/controller/v1/response/CartResponse.kt`
- `core/core-api/src/main/kotlin/io/dodn/commerce/core/api/controller/v1/response/OrderCheckoutResponse.kt`

---

## 6. API 엔드포인트 설계 불일치

### 6.1 컨트롤러 패키지 위치 불일치
**현상:**
- v1 API: `core.api.controller.v1` 패키지
- 배치 API: `core.api.controller.batch` 패키지
- 헬스체크: `core.api.controller` 패키지 (버전 없음)

**문제점:**
- 배치 API와 일반 API의 버전 관리 전략이 불명확
- HealthController는 버전이 없어 향후 버전 관리 시 혼란 가능

**개선 방향:**
- 배치 API도 버전 관리 필요 여부 결정
- 헬스체크 등 공통 API는 별도 패키지 정의 (예: `common`)
- 모든 API의 버전 정책을 가이드라인에 명시

**관련 파일:**
- `core/core-api/src/main/kotlin/io/dodn/commerce/core/api/controller/HealthController.kt`
- `core/core-api/src/main/kotlin/io/dodn/commerce/core/api/controller/batch/SettlementBatchController.kt`

### 6.2 REST 리소스 네이밍 불일치
**현상:**
- `/v1/cart` - 단수형
- `/v1/cart/items` - 복수형
- `/v1/orders` - 복수형
- `/v1/cart-orders` - 하이픈 조합 복수형

**문제점:**
- 가이드라인은 복수형을 권장하지만 `/v1/cart`는 단수형
- `/v1/cart-orders`는 REST 리소스 설계 원칙과 맞지 않음 (행위가 아닌 리소스 중심 설계)

**개선 방향:**
- 모든 리소스를 복수형으로 통일: `/v1/carts`
- `/v1/cart-orders`는 `/v1/orders`로 통합하고, 요청 본문으로 구분 (예: `fromCart` 플래그)
- 또는 쿼리 파라미터 활용: `POST /v1/orders?source=cart`

**관련 파일:**
- `core/core-api/src/main/kotlin/io/dodn/commerce/core/api/controller/v1/CartController.kt`
- `core/core-api/src/main/kotlin/io/dodn/commerce/core/api/controller/v1/OrderController.kt` (라인 45)

---

## 7. 예외 및 에러 처리

### 7.1 검증 로직 위치 불일치
**현상:**
- 요청 DTO 내부에서 검증: `CreateOrderRequest.toNewOrder()` - `if (quantity <= 0) throw CoreException`
- 요청 DTO 내부에서 검증: `AddCartItemRequest.toAddCartItem()` - `if (quantity <= 0) throw CoreException`
- 엔티티 메서드에서 검증 및 보정: `CartItemEntity.applyQuantity()` - `if (value < 1) 1 else value`

**문제점:**
- 검증 위치가 DTO, 서비스, 엔티티에 분산됨
- 동일한 검증 로직이 중복됨 (quantity 검증)
- 검증 실패 시 처리 방식이 일관되지 않음 (예외 vs 보정)

**개선 방향:**
- Bean Validation (JSR-380) 활용하여 DTO에 선언적 검증 추가
- 비즈니스 규칙 검증은 도메인 계층에서 처리
- 엔티티는 불변성 보장에만 집중, 보정 로직은 서비스로 이동
- 검증 정책을 가이드라인에 명시

**관련 파일:**
- `core/core-api/src/main/kotlin/io/dodn/commerce/core/api/controller/v1/request/CreateOrderRequest.kt` (라인 14)
- `core/core-api/src/main/kotlin/io/dodn/commerce/core/api/controller/v1/request/AddCartItemRequest.kt` (라인 12)
- `storage/db-core/src/main/kotlin/io/dodn/commerce/storage/db/core/CartItemEntity.kt` (라인 16-18)

---

## 8. 하드코딩 및 매직 넘버/문자열

### 8.1 하드코딩된 값들
**현상:**
- `PaymentService.success()` 메서드 내부:
  ```kotlin
  PaymentMethod.CARD,  // 라인 69
  "PG 승인 API 호출의 응답 값 중 `승인번호` 넣기",  // 라인 70
  ```
- `OrderService.create()` 메서드 내부:
  ```kotlin
  " 외 ${newOrder.items.size - 1}개"  // 라인 33 - 하드코딩된 문자열
  ```

**문제점:**
- 실제 구현이 필요한 부분이 하드코딩으로 남아있음
- 다국어 지원 시 하드코딩된 문자열 관리 어려움
- NOTE 주석으로만 표시되어 TODO로 추적되지 않음

**개선 방향:**
- TODO 주석으로 변경하여 IDE에서 추적 가능하도록 함
- 하드코딩된 문자열은 메시지 프로퍼티 파일로 추출
- PG 연동 로직은 별도 서비스로 분리

**관련 파일:**
- `core/core-api/src/main/kotlin/io/dodn/commerce/core/domain/PaymentService.kt` (라인 62-71)
- `core/core-api/src/main/kotlin/io/dodn/commerce/core/domain/OrderService.kt` (라인 33)

### 8.2 NOTE 주석의 일관되지 않은 사용
**현상:**
- NOTE 주석이 여러 곳에서 사용되지만 목적이 다양함
  - 구현 필요 표시: `PaymentService.success()` (라인 63)
  - 비즈니스 규칙 설명: `PointHandler` (라인 22, 40)
  - 설계 고민 표시: `ProductController.findProduct()` (라인 43)
  - 배치 실행 시간 명시: `SettlementBatchController` (라인 14-17)

**문제점:**
- NOTE의 사용 목적이 명확하지 않음
- 액션이 필요한 항목과 단순 설명이 혼재

**개선 방향:**
- TODO: 구현이 필요한 항목
- NOTE: 비즈니스 규칙 설명 및 중요 정보
- FIXME: 알려진 문제로 수정이 필요한 항목
- 주석 컨벤션을 가이드라인에 추가

**관련 파일:**
- `core/core-api/src/main/kotlin/io/dodn/commerce/core/domain/PaymentService.kt`
- `core/core-api/src/main/kotlin/io/dodn/commerce/core/domain/PointHandler.kt`
- `core/core-api/src/main/kotlin/io/dodn/commerce/core/api/controller/v1/ProductController.kt`

---

## 9. 도메인 모델 설계

### 9.1 User 도메인 모델의 빈약함
**현상:**
- `User` 클래스가 단순 데이터 홀더: `data class User(val id: Long)`
- `PaymentService`에서 `User(payment.userId)` 형태로 직접 생성 (라인 78-79)

**문제점:**
- User가 식별자만 가지고 있어 도메인 개념으로서의 의미 부족
- User를 직접 생성하는 것이 적절한지 불명확
- 인증된 사용자와 단순 userId의 구분이 모호

**개선 방향:**
- User 도메인 모델에 필요한 속성 및 행위 추가
- UserId 값 객체(Value Object) 도입 고려
- 인증 컨텍스트와 도메인 모델을 명확히 분리

**관련 파일:**
- `core/core-api/src/main/kotlin/io/dodn/commerce/core/domain/User.kt`
- `core/core-api/src/main/kotlin/io/dodn/commerce/core/domain/PaymentService.kt` (라인 78-79)

### 9.2 엔티티 메서드의 책임 불명확
**현상:**
- `CartItemEntity.applyQuantity()`: 비즈니스 로직 (최소값 보정) 포함
- `PaymentEntity.success()`: 상태 전이 + 다중 필드 변경
- `BaseEntity`: Soft Delete 로직 포함

**문제점:**
- 엔티티에 비즈니스 로직과 영속성 관련 로직이 혼재
- 엔티티가 복잡해지면 테스트 어려움
- 도메인 모델 vs 영속성 모델의 경계가 모호

**개선 방향:**
- 엔티티는 영속성과 불변성 보장에 집중
- 비즈니스 로직은 도메인 서비스 또는 도메인 모델로 이동
- 필요 시 엔티티와 도메인 모델을 분리하는 것 고려

**관련 파일:**
- `storage/db-core/src/main/kotlin/io/dodn/commerce/storage/db/core/CartItemEntity.kt` (라인 16-18)
- `storage/db-core/src/main/kotlin/io/dodn/commerce/storage/db/core/PaymentEntity.kt` (라인 50-56)
- `storage/db-core/src/main/kotlin/io/dodn/commerce/storage/db/core/BaseEntity.kt`

---

## 10. 테스트 가능성 및 관측성

### 10.1 서비스 단위 테스트 어려움
**현상:**
- 서비스들이 구체 클래스(엔티티, 리포지토리)에 직접 의존
- 생성자 주입은 잘 되어있으나 모의 객체 작성이 복잡

**문제점:**
- 단위 테스트를 위해 Spring Context 로딩 필요
- 테스트 속도 저하
- 의존성 격리 어려움

**개선 방향:**
- 인터페이스 기반 의존성 주입
- 테스트 더블(Mock, Stub) 작성 용이성 확보
- 통합 테스트와 단위 테스트 명확히 구분

---

## 11. 기타 개선 항목

### 11.1 컴포넌트 스테레오타입 혼용
**현상:**
- 대부분의 도메인 계층 클래스에 `@Service` 어노테이션 사용
- 일부 클래스는 `@Component` 사용: `PointHandler`, `ReviewPolicyValidator`

**문제점:**
- `@Service`와 `@Component`의 사용 기준이 불명확
- 의미론적 구분 없이 사용됨

**개선 방향:**
- `@Service`: 유스케이스 조율, 비즈니스 로직 처리
- `@Component`: 유틸리티성 컴포넌트, 특정 역할이 명확한 경우
- 가이드라인에 어노테이션 사용 기준 명시

**관련 파일:**
- `core/core-api/src/main/kotlin/io/dodn/commerce/core/domain/` (전체)

### 11.2 응답 타입의 일관성
**현상:**
- 일부 API는 구체적인 응답 DTO 반환: `ApiResponse<CartResponse>`
- 일부 API는 `ApiResponse<Any>` 반환 (라인 29, 39, 45 - CartController)
- 일부 API는 `ResponseEntity<*>` 반환 (HealthController)

**문제점:**
- API 응답 타입이 일관되지 않음
- `ApiResponse<Any>`는 타입 안정성 저하
- Swagger/OpenAPI 문서 생성 시 정확한 스키마 생성 어려움

**개선 방향:**
- 모든 API는 명확한 응답 DTO 정의
- 응답이 없는 경우 `ApiResponse<Void>` 또는 별도 정의된 Empty 응답 사용
- HealthController도 `ApiResponse` 규격 적용 고려

**관련 파일:**
- `core/core-api/src/main/kotlin/io/dodn/commerce/core/api/controller/v1/CartController.kt` (라인 29, 39, 45)
- `core/core-api/src/main/kotlin/io/dodn/commerce/core/api/controller/HealthController.kt`

---

## 개선 우선순위 제안

### High Priority (P0)
1. **트랜잭션 관리 불일치** - 데이터 일관성에 직접적 영향
2. **검증 로직 위치 불일치** - 보안 및 데이터 무결성 위험
3. **하드코딩된 PG 연동 로직** - 실제 동작하지 않는 코드

### Medium Priority (P1)
4. **도메인 서비스의 인프라 계층 직접 의존** - 유지보수성 및 테스트 가능성 저하
5. **서비스 계층 네이밍 불일치** - 코드 이해도 저하
6. **API 엔드포인트 설계 불일치** - API 일관성 및 확장성

### Low Priority (P2)
7. **DTO 변환 패턴 불일치** - 코드 가독성
8. **컴포넌트 스테레오타입 혼용** - 의미론적 명확성
9. **NOTE 주석 일관성** - 코드 관리 편의성

---

## 다음 단계

1. **우선순위별 리팩토링 계획 수립**
   - 각 항목에 대한 구체적인 리팩토링 작업 정의
   - 작업 범위 및 영향도 분석

2. **가이드라인 문서 업데이트**
   - 본 문서에서 식별된 규칙들을 `.junie/guidelines.md`에 반영
   - 신규 코드 작성 시 일관된 패턴 적용

3. **점진적 개선**
   - 새로운 기능 추가 시 신규 패턴 적용
   - 기존 코드는 수정 시점에 리팩토링 (보이스카우트 룰)

4. **정적 분석 도구 활용**
   - ktlint 규칙 확장
   - ArchUnit 등으로 아키텍처 규칙 자동 검증

---

**Note:** 본 문서는 현재 시점의 코드 분석 결과이며, 실제 리팩토링 시 비즈니스 요구사항과 팀의 우선순위를 고려하여 진행해야 합니다.
