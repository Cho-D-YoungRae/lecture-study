쿠폰 기능 확장 정리

본 문서는 현재 쿠폰 개념에 대한 신규 요구사항(정률 쿠폰, 다회권 쿠폰) 반영을 위해 필요한 작업 목록과 검토 사항을 정리한다.

- 프로젝트 가이드라인: 4계층, Assembler 사용, Logic Layer의 Repository 접근 허용, @Transactional 규칙 등을 준수한다.

1. 정률 쿠폰 추가
정률(%) 기반으로 할인 금액을 계산하는 쿠폰 유형을 추가한다.

1-1. 작업 목록
- Enum 확장
  - `CouponType`에 `PERCENT_RATE` 추가.
- 스키마/엔티티/개념 객체 확장
  - 현재 `CouponEntity`/`Coupon`는 `discount: BigDecimal` 1개만 존재.
  - 정률 쿠폰 지원을 위해 다음 중 하나를 택한다.
    - A안(간단): `discount`를 정률 쿠폰일 때 비율(0~100)로 사용. 추가 정책용 필드 보강.
    - B안(명확): `value`로 필드 명 변경(마이그레이션 필요) + 타입별 해석. [검토사항 참조]
  - 추가 필드 제안(옵션)
    - `maxDiscountAmount: BigDecimal?` 정률 할인 상한액
    - `minOrderAmount: BigDecimal?` 최소 주문 금액 조건
- Data Access Layer
  - `CouponEntity` 컬럼 추가(상한액/최소주문금액 등) 및 마이그레이션 스크립트 준비.
  - 기본값/널 허용 정책 정의(기존 데이터 호환).
- Logic Layer
  - 할인 계산 책임 분리: `CouponDiscountCalculator`(또는 유사 명명) 추가(정액/정률 모두 처리).
  - 정률 계산 시 반올림/절사 정책 적용 및 상한액 반영.
  - `CouponReader`, `CouponTargetReader`는 타입 추가 인식 외 변화 없음.
- Business Layer
  - 서비스들은 Logic Layer 컴포넌트 조합만 수행(가이드라인 준수).
- Presentation Layer(API)
  - 응답 DTO(`CouponResponse`, `OwnedCouponResponse`)에 필요 시 정책 필드(`maxDiscountAmount`, `minOrderAmount`) 추가.
  - Converter(`*Response.of(...)`)에서 개념 객체 기반 변환.
- 검증/유효성
  - 정률 범위(0 < rate ≤ 100) 검증 추가.
  - 상한액/최소주문금액 유효성 검증.
- 테스트
  - 할인 계산 유닛 테스트(경계값, 라운딩, 상한 적용, 최소주문금액 미충족 포함).
  - Repository 테스트(타입 저장/조회).
  - API 테스트(표현/포맷 확인).

1-2. 검토해야 할 사항
- 금액 계산 정책
  - 반올림 규칙(예: 소수점 2자리, HALF_UP 등)과 통화 최소단위 절사 필요 여부.
  - 배송비/옵션가/포장비 포함 여부.
- 상한/최소주문금액 정책
  - 상한액 필수 여부, 기본값. 최소주문금액과 동시 적용 시 우선순위.
- 쿠폰 중복 적용/적용 순서
  - 정액·정률 혼용 시 적용 순서(정률→정액 or 정액→정률) 결정 필요.
- 타겟팅/적용 범위
  - 상품/카테고리 타겟의 다중 매칭 시 중복 감산 방지.
- 세금/영수증 반영
  - 과세/면세/부가세 처리 기준 포함 여부.
- 화면/표기
  - 퍼센트 표시, 상한 안내 문구, 최소주문금액 안내.
- 생성/수정 시 유효성
  - 운영툴 입력 검증(0~100, 소수점 자릿수 제한 등).
- 성능
  - 대량 상품에 대한 일괄 계산 시 비용. 필요 시 계산기 내부 캐싱/전처리 고려.

2. 다회권 쿠폰 추가
한 사용자가 동일 쿠폰을 여러 번 사용할 수 있도록 하는 요구.

2-1. 작업 목록
- 개념 정립
  - “다회권” 의미: 1회 다운로드 후 N회까지 사용 가능(권장) 또는 다운로드를 N장 발급.
  - 단순화: 사용자별 1개 보유(유니크 유지) + 사용가능횟수 카운트 방식 권장.
- 스키마/엔티티 변경
  - `OwnedCouponEntity`
    - 현재: `state`(DOWNLOADED/USED), `@Version` 있음, (userId, couponId) 유니크 인덱스.
    - 추가 필드:
      - `totalUses: Int` 발급된 총 사용 가능 횟수
      - `usedCount: Int` 사용된 횟수(기본 0)
    - 비즈니스 메서드:
      - `useOne()` → 낙관적 락(@Version) 하에 `usedCount++`, 잔여 0이면 상태 전이.
      - `revertOne()` 환불/취소 시 `usedCount--` 처리.
    - 상태 관리
      - `OwnedCouponState` 확장 검토: `ACTIVE(=DOWNLOADED)`, `EXHAUSTED` 등. 또는 현 상태 유지하되 카운트로 소진 판단.
  - 마이그레이션
    - 컬럼 추가 및 기본값 반영(기존 데이터는 `totalUses=1, usedCount=0`으로 간주).
    - 유니크 인덱스는 유지(카운트 방식 택한 경우).
- 개념 객체 변경
  - `OwnedCoupon`에 `totalUses`, `usedCount`(또는 `remainingUses`) 노출 고려.
  - `Coupon`에도 다회권 정의 값 필요 시 추가(예: `usesPerDownload` 또는 `limitPerUser`).
- Logic Layer
  - 사용/되돌리기 처리: 낙관적 락 이용한 원자적 증감 구현.
  - 체크아웃/적용 로직: 남은 횟수 ≥ 1 확인 후 사용 허용.
- Business Layer
  - 다운로드(`CouponDownloader`): 발급 시 `totalUses` 초기화(쿠폰 설정 값), `usedCount=0` 저장.
- Presentation Layer(API)
  - 응답 DTO에 `remainingUses` 등 필드 추가.
  - 체크아웃 조회 시 남은 횟수 기반 필터링/정렬 요구 반영.
- 테스트
  - 동시성 테스트: 동일 `OwnedCoupon` 다중 사용 시 `@Version`으로 초과 사용 방지.
  - 경계값 테스트: 1→0 전이, 0에서 추가 사용 시 예외.
  - 환불/취소에 의한 되돌리기(revert) 테스트.

2-2. 검토해야 할 사항
- 소비 단위 정의
  - “한 주문당 1회 사용”인지, “한 주문에서 여러 번 사용 가능”인지 명확화.
- 환불/부분취소
  - 부분취소 시 사용 횟수 되돌림 정책(전액/부분 환불 기준) 정의.
- 만료와 잔여 횟수
  - 만료일 경과 시 잔여 횟수 무효 처리.
- 동시성/중복 사용 방지
  - 낙관적 락 재시도/실패 처리 정책 정의.
- 중복/동시 다운로드
  - 다회권과 다운로드 정책의 관계(재다운로드 허용 여부) 정의.
- Stack 정책 및 다른 쿠폰과의 관계
  - 다회권이더라도 다른 쿠폰과 동시 적용 가능 여부 명확화.
- 노출/UX
  - 남은 횟수, 만료일 표시, 소진 시 안내.
- 운영툴
  - 쿠폰 생성/수정 시 `usesPerDownload`(또는 `totalUses`) 입력/수정 기능.

3. 공통 가이드라인 준수 포인트
- 계층 규칙: Controller → Service → Logic → Data Access 순방향 참조만 허용. 동일 레이어 간 참조 금지(Logic은 예외적으로 허용).
- Assembler 사용: Controller는 Service만 조합. Logic/Repository 직접 참조 금지.
- @Transactional: Logic Layer에만 사용. 쓰기 연산에 필요한 범위에서만 적용.
- Entity ↔ 개념 객체: Logic Layer 반환 시 Entity 직접 반환 금지. 개념 객체로 변환.
- 하드코딩/매직 넘버 지양: 상수/정책 값은 Enum 또는 객체로 추출.

4. 변경 요약(권장 설계 선택지)
- 정률 쿠폰: `CouponType.PERCENT_RATE` 추가, 상한/최소주문금액 필드 도입, 전용 계산기 도입.
- 다회권 쿠폰: `OwnedCouponEntity`에 `totalUses`, `usedCount` 추가, 낙관적 락으로 사용 처리, 응답에 남은 횟수 노출.
- 마이그레이션: 신규 컬럼 추가 + 안전한 기본값, 기존 데이터 호환.
- 테스트: 계산/동시성/경계/표현까지 포괄.