### 장바구니(카트) 리팩토링 작업 계획

본 계획은 팀 가이드라인(<guidelines>)을 준수하여 기존 장바구니 코드를 3-레이어(명시적 4영역) 구조로 분리하고, 병렬 작업이 가능하도록 의존 관계를 최소화한 작업 단위로 쪼갠 문서입니다.

#### 목표 요약
- Cart 관련 코드의 레이어 정렬: Presentation(Controller) → Business(*Service) → Logic(*Reader/*Manager 등) → Data Access(*Repository)
- Transactional 은 Logic Layer 에만 적용하도록 이동
- Entity ↔ 개념 객체 변환 규칙 준수 (Logic Layer 반환 시 Entity 사용 금지)
- (수정) 이번 이터레이션에서는 Service 조합이 필요 없으므로 Assembler 미도입
- 테스트는 전부 생성자 주입 사용

---

### 1. 현재 문제점(요약)
1) `CartService` 가 Business Layer 이지만, 현재 `@Transactional` 이 존재함 → 가이드라인 위반 (Logic Layer 로 이동 필요)
2) `CartService.getCart` 에서 Repository 직접 접근 및 개념 변환을 모두 처리 → 책임 혼재
3) Presentation → Business → Data 로 흐르되, Logic Layer 컴포넌트가 부재하여 재사용/조합성이 낮음

---

### 2. 타깃 구조(리팩토링 후)
- Presentation Layer
  - `CartController` (기존)
- Business Layer
  - `CartService` — Logic 컴포넌트 조합, 트랜잭션 없음
- Logic Layer
  - `CartReader` — 장바구니 조회(사용자별 장바구니 아이템, 상품 조회/맵핑) [읽기 전용, 보통 @Transactional 없음] — (수정) `ProductFinder` 활용
  - `CartItemManager` — 아이템 추가/수정/삭제의 상태 전이(@Transactional)
  - (옵션) `CartValidation` — 장바구니 수량/상태 검증
  - (기존) `ProductFinder` — 상품 조회 담당 Finder (Logic Layer)
- Data Access Layer
  - `CartItemRepository` (기존)
  - `ProductRepository` (기존)

개념 객체 예시: `Cart`, `CartItem`, `Product`, `Price` (기존 유지)

---

### 3. 병렬 작업이 가능한 작업 단위 (워크스트림)

각 작업 단위는 독립 브랜치로 수행 가능하도록 설계했습니다. 필요한 공통 타입(개념 객체)은 기존 것을 재사용하며, 외부 API/컨트롤러 시그니처 변경은 최소화합니다.

- 공통 규칙
  - 브랜치 명: `feature/cart/<TASK-ID>-<short-desc>`
  - 커밋 규칙: `feat(cart): <desc>` / `refactor(cart): <desc>` / `test(cart): <desc>`

#### WS-A. Logic Layer 컴포넌트 추가 (병렬 가능)
- CART-01: `CartReader` 추가
  - 역할: 사용자 장바구니 조회, 상품 로딩 및 개념 객체 맵핑
  - 입력: `userId: Long`
  - 출력: `Cart`
  - 의존: `CartItemRepository`, `ProductFinder` (수정)
  - 트랜잭션: 필요 시 없음 (읽기)

- CART-02: `CartItemManager` 추가
  - 역할: 아이템 추가/수정/삭제, 상태 전이 및 수량 적용
  - 메서드:
    - (수정) `addItem(userId: Long, item: AddCartItem): Long` — 기존 개념 클래스 활용
    - (수정) `modifyItem(userId: Long, item: ModifyCartItem): Long` — 기존 개념 클래스 활용
    - `deleteItem(userId: Long, cartItemId: Long)`
  - 트랜잭션: 각 쓰기 메서드 단위 @Transactional 부여 (Logic Layer 규칙 준수)
  - 의존: `CartItemRepository`

  (수정) CART-03 작업은 이번 범위에서 제외

#### WS-B. Business Layer 정리 (A와 병렬 가능)
- CART-04: `CartService` 를 Logic 조합만 수행하도록 리팩토링
  - 기존 `@Transactional` 삭제
  - 내부에서 `CartReader`, `CartItemManager` 호출만 수행
  - 컨트롤러 호출 시그니처 변경 없이 유지 (가능하면)

#### WS-C. Presentation Layer 정리
- (수정) 이번 이터레이션에서는 Service 조합이 필요 없어 `CartAssembler` 미도입. 컨트롤러는 기존 방식 유지

#### WS-D. Data Access 점검 (A와 병렬 가능)
- CART-06: `CartItemRepository`, `ProductRepository` 조회/저장 메서드 점검
  - 필요한 인덱스/쿼리 최적화 포인트 식별 (성능 관점)
  - Entity 반환은 내부에서만 사용, 외부 노출 금지 확인

#### WS-E. 테스트 및 기술부채 정리 (B, C 완료 후 병렬 가능)
- CART-07: 단위 테스트 작성
  - `CartReader` — 빈 카트/단일/다건/삭제 상품 필터링 케이스
  - `CartItemManager` — 추가/수정/삭제 성공과 예외 케이스
  - 모두 생성자 주입 방식

- CART-08: 통합 테스트(선택)
  - Service/Assembler/Controller 흐름 스모크 테스트

---

### 4. 의존 관계 및 병렬화 가이드
- A(Logic 추가) ↔ B(Business 정리): 인터페이스(시그니처)만 초기에 합의하면 병렬 가능
  - `CartReader.getCart(userId: Long): Cart`
  - `CartItemManager.add(item: AddCartItem)/modify(item: ModifyCartItem)/delete(...): Long | Unit`
- C(Presentation) 은 이번 범위에서 구조 변경 없음 (Assembler 미도입)
- D(Data Access) 는 쿼리 추가/변경이 없다면 완전 병렬, 변경이 생기면 A에 알림 필요
- E(테스트) 는 각 WS 단위로 병렬 작성 가능, 통합 테스트는 마지막

---

### 5. 단계별 마이그레이션 플랜(릴리스 안전성)
1) WS-A/B를 통해 내부 구조 교체 (기존 `CartService` 시그니처 유지)
2) WS-C는 변경 없음(Assembler 미도입)
3) 테스트(WS-E) 보강 후 마무리

---

### 6. 주요 리팩토링 디테일(규칙 적용)
- 트랜잭션: Logic Layer(`CartItemManager`)에만 부여. Business Layer(`CartService`)에서는 제거
- 변환: Presentation 의 요청→개념 변환 `to***()` 네이밍 사용, 응답 변환은 `*Response.of(...)`
- 레이어 참조 규칙: 위→아래 단방향, 하위 레이어 건너뛰기 금지, 동일 레이어 간 참조 금지(Logic Layer는 예외적으로 허용)
- Logic Layer에서 Entity 반환 금지
- 하드코딩 금지, 가능한 Enum/Object 추출

---

### 7. 산출물 체크리스트
- [ ] `CartReader` 클래스 (@Component) — `ProductFinder` 사용
- [ ] `CartItemManager` 클래스 (@Component, @Transactional on methods)
- [ ] 리팩토링된 `CartService` (@Service, 트랜잭션 없음)
- [ ] 컨트롤러 입력 DTO의 `to***()` 변환 함수 적용
- [ ] 응답 DTO의 `of(...)` 변환 적용
- [ ] 단위/통합 테스트

---

### 8. 변경 범위(예상 파일)
- core/core-api/src/main/kotlin/io/dodn/commerce/core/api/controller/v1/CartController.kt (호출부 정리)
- core/core-api/src/main/kotlin/io/dodn/commerce/core/domain/CartService.kt (리팩토링)
- core/core-api/src/main/kotlin/io/dodn/commerce/core/domain/CartReader.kt (신규) — `ProductFinder` 의존
- core/core-api/src/main/kotlin/io/dodn/commerce/core/domain/CartItemManager.kt (신규)
- 테스트 경로 하 다수 파일

---

### 9. 리스크 및 완화
- 시그니처 변경 리스크: 초기 합의된 인터페이스로 방지, 외부 API 변경 최소화
- 트랜잭션 이동으로 인한 동작 변화: 단위 테스트로 검증, 통합 테스트로 회귀 방지
- 성능: `findAllById` N+1, in-clause 사이즈 점검, 필요한 경우 Reader 내부에서 배치/캐시 전략 추가

---

### 10. 타임라인(예시)
- Day 1: WS-A, WS-B 착수(병렬) — 골격/인터페이스 고정
- Day 2: WS-D 진행, 단위 테스트 작성
- Day 3: 통합 테스트 및 회귀, 문서화 및 마무리
