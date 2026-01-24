# 입사 첫날 - 레거시 + AI 느끼기
## #P1

나는 이 회사에 오늘 처음 입사한 개발자야, 이 프로젝트에 대한 .junie/guidelines.md를 작성해

- 패키지 구조, 코드 스타일, 설계 스타일을 정리해서 기록해
- 현재 모듈 구조에 대한 설명을 적어
- 한글로 작성해

## #P2
현재 프로젝트의 기존 코드를 레거시 코드로 정의하고 기존 코드를 개선하려 해
현재 프로젝트에서 코드가 일관성이 없거나 규칙이 명확하지 않은 부분을 LEGACY.md 파일에 정리해

## #P3
guidelines.md 기준으로 코드 리팩토링해

## #P4
findProduct 함수의 Service를 직접 쓰는 부분을 Assembler로 통합해

[File]
ProductController.kt


# 상품목록 - 레거시 + AI 느끼기
## P1
삼풍 목록 조회 기능 findProducts 함수에 아래 추가 요구사항을 구현해
- 최근 60일 동안의 찜하기 수를 추가해
    - 찜하기는 Favorite 개념이야
- 최근 30일 동안의 주문 수를 추가해
    - 주문은 Order 개념이야
- 상품과 찜하기, 주문은 연관된 개념은 아니야, 그러므로 Assembler를 통해서 조합하는 식으로 구현해

[File]
ProductController.kt
ProductService.kt
ProductFinder.kt

## P2
[작업1]
ProductAssembler에서는 Service만을 사용해야 해
- favoriteFinder
- orderFinder
  두 개는 직접 사용할 수 없고, Service로 격상 시켜야 해

[작업2]
now를 통해 날짜를 가져오는 부분은 정책적인 상수야 Enum이나 Object로 적절히 추출해

[File]
ProductAssembler


# 상품상세 - 레거시 + AI 느끼기

## P1
상품 옵션을 추가해
- 상품 당 옵션은 n개 있을 수 있다
- 상품 옵션에 대한 개념은 ProductOption 개념 클래스로 만들어
    - 상품 개념이 상품 옵션 개념을 직접 알 필요는 없어
- 상품 상세 API (findProduct)에 옵션 정보를 추가해

## P2
상품 옵션 개념에 필드 정보를 상품과 유사하게 구성해 주고 더 필요한 필드가 있다면 추가해


# 리뷰 - 레거시 + AI 느끼기

## P1
Review에 Image를 추가할 수 있도록 구현해
[요구사항]
- Review 작성 시 Image 목록을 보낼 수 있도록 구현해
    - Image Review는 포인트를 500원 더 지급하도록 구현해
- Review 목록 조회 시 Image Review만 모아보기 기능을 추가해
- 이미 작성된 Image Review는 이미지를 모두 삭제하고 수정할 순 없도록 구현해

[정책]
- Image는 리뷰 작성 시 필수는 아니야
- Image는 최대 5개까지 등록이 가능해

[File]
ReviewController.kt
ReviewService.kt

## P2
- Review 삭제 시에도 Image Review라면 추가 포인트를 회수해
- ImageController에 Image Upload API를 구현해
- ReviewFinder의 Image Review를 페이징 조회가 정상적으로 되게 해줘, 20개 조회하면 Image Review가 20개 조회되게 해줘
- ReviewManager의 Update 시 삭제할 Image Review Id 목록을 받아서 삭제하게 해줘
- ReviewPolicyValidator의 Image Review 인지 확인하는 부분을 ReviewManager로 옮겨줘


# 찜하기 - 레거시 + AI 느끼기
## P1
- FavoriteController의 applyFavorite 함수 로직을 FavoriteAssembler로 추출해
- FavoriteService의 함수들을 각 컴포넌트로 추출해

[File]
FavoriteController.kt
FavoriteService.kt`

## P2
찜 목록 조회 API(getFavorites)에서 찜 목록 조회 후 Product 정보를 병합하여 상품 정보를 API 응답에 추가해
- FavoriteService 와 ProductService를 활용한 코드는 Assembler에 추가해
- 상품명, 상품 이미지, 가격정보를 포함해서 API 응답에 내려줘

[File]
FavoriteController.kt
ProductService.kt

## P3
찜하기(Favorite)에 Brand 찜, Merchant 찜을 추가할 수 있도록 해줘
- 현재 찜하기는 Product 찜만 지원해
- 이제 추가로 Brand, Merchant에 대해서도 찜 목록을 할 수 있도록 구현해

[File]
FavoriteController.kt
FavoriteManager.kt

## P4
찜하기 목록 조회 API에 타입 별로 각각 조회해
- Brand와 Merchant는 Id 와 Name 정도가 존재하도록 엔티티 구성해
- Brand와 Merchant도 목록 조회 시 조회 타입에 맞춰서 매핑을 채워서 API 응답을 완성해

[File]
FavoriteController.kt
FavoriteFinder.kt

## P5
찜 목록 조회 시 찜하기 후 상품이 변경되었으면 `변경됨 배지`를 추가하기 위해 `isChanged` 필드를 추가해서 응답해
- 찜하기 후에 상품이 변경되어서 상품의 수정일이 찜하기 일시 보다 뒤라면 isChanged를 true로 응답해

[File]
FavoriteController.kt
Product.kt
FavoriteResponse.kt


# 쿠폰 - 레거시 + AI 느끼기
## P1
우리 팀의 가이드라인에 맞춰서 기존 Service 코드를 리팩토링해

[File]
CouponService.kt
OwnedCouponService.kt

## P2
- Service 로직 안에 Coupon, OwnedCoupon 개념을 각각의 컴포넌트로 분리해서 Service에서는 조립해서 로직을 구현해
- 한 클래스에 책임을 모으지 말고 적절히 분리해서 조합하는 식으로 구성해

[File]
CouponService.kt
OwnedCouponService.kt

## P3
- CouponService를 리팩토링해
    - productTargets, categoryTargets 조회하는 부분을 CouponTargetReader로 추출해
    - couponRepository.findByIdInAndStatus(..).map(...) 조회 부분을 CouponReader로 추출해

[File]
CouponService.kt

## P4
- OwnedCouponService 리팩토링해
    - getOwnedCoupons를 OwnedCouponReader로 추출해
    - download를 CouponDownloader로 추출해
        - ownedCouponRepository 사용하는 부분은 OwnedCouponAdder로 이관해
    - getOwnedCouponsForCheckout 함수를 재구성해
        - 쿠폰 대상을 가져오는 부분은 CouponTargetReader로 이관해
        - ownedCoupon 조회 부분은 OwnedCouponReader로 추출해

[File]
OwnedCouponService.kt

## P5
- 현재 쿠폰 개념에 아래 2가지 추가 요구사항이 들어왔어
    - 정률 쿠폰 추가
    - 다회권 쿠폰 추가

- 현재 코드 기준 요구사항에 대한 작업 목록과, 검토해야 할 사항을 정리해
    - 요구사항에 대한 작업 목록 정리해
    - 검토해야 할 사항 목록 정리해

두 가지 사항을 COUPON.md 파일에 정리해

[File]
CouponService.kt
OwnedCouponService.kt

## P6 - Question & Answer
작업해

[Question]
구현을 시작하기 전에 몇 가지 정책 결정을 확인하고 싶습니다. 결정되면 바로 코드 반영하겠습니다.
1) 정률(%) 쿠폰 계산 정책
   • 반올림: 통화 단위 기준 소수 0자리 HALF_UP로 반올림해도 될까요? (예: 12,345원의 15% = 1,851.75 → 1,852원)
   • 상한액(maxDiscountAmount): 사용하시겠다면 필수인가요(필수/선택)? 기본값은 null(무제한)로 둘까요?
   • 최소주문금액(minOrderAmount): 사용하시겠다면 미충족 시 할인 0 처리로 할까요?
   • 비율 범위: 0 < rate ≤ 100으로 고정해도 문제 없을까요?

2) 다회권 쿠폰 정책
   • 소비 단위: “한 주문당 1회 사용”으로 고정해도 될까요? (한 주문에서 여러 번 사용은 불가)
   • 잔여 횟수: (userId, couponId) 유니크 유지 + totalUses, usedCount로 관리하는 카운트 방식으로 진행해도 될까요?
   • 환불/부분취소: 취소/환불 시 usedCount를 1 감소시키는 revertOne()로 처리하면 될까요? 부분취소 시에도 동일하게 1회 되돌림인가요, 아니면 금액 비례 복원 정책이 필요한가요?
   • 상태: EXHAUSTED 상태를 추가할까요, 아니면 현 상태(다운로드/사용)만 두고 남은 횟수==0이면 적용 불가로 볼까요?
   • 동시성: 낙관적 락(@Version) 실패 시 재시도는 0~N회 중 어떤 정책을 선호하시나요? (예: 즉시 실패, 최대 3회 재시도)

3) 전파 범위
   • API 응답에 maxDiscountAmount, minOrderAmount, remainingUses를 노출할까요? 아니면 내부 계산용으로만 두고 UI는 추후 협의할까요?

확인 주시면 DB 스키마(엔티티) → 로직 컴포넌트 → 서비스 조립 → 테스트 순으로 바로 진행하겠습니다.

[Answer]
1-1) 진행해
1-2) 상한액은 필요 없어
1-3) 최소 주문금액 미 충족 시 쿠폰 사용 불가
1-4) 할인 범위는 1~100으로 고정해

2-1) 진행해
2-2) 진행해
2-3) 금액 비례 복원 정책이 필요해
2-4) 횟수 ==0 이면 적용 불가로 봐
2-5) 즉시 실패하고 에러 로그 남겨줘

3-1) minOrderAmount만 내려줘

## P7 - Question & Answer
이어서 작업해

[Question]
부분취소 비례 복원 정책 관련해서 몇 가지를 확정해 주시면 바로 반영하겠습니다.
A) 비례 기준 금액
•
기준을 무엇으로 볼까요?
i.
주문 원금(payment.originAmount) 대비 환불액(refundAmount)
ii.
실제 결제금액(payment.paidAmount) 대비 환불액(refundAmount)
iii.
기타: 특정 기준 제시
B) 복원 임계 규칙
•
한 주문당 1회 사용 정책이므로, 부분취소 시 0회 또는 1회만 복원이 가능합니다. 어떤 규칙을 원하시나요?
i.
전액 취소(= 기준 대비 100%)일 때만 1회 복원, 그 외 0회
ii.
환불 비율이 X% 이상이면 1회 복원(예: 50% 이상이면 1회 복원) — X 값 지정 필요
iii.
환불액이 정률/정액 할인액 이상이면 1회 복원 — 계산 기준 명시 필요
C) 적용 시점
•
현재 CancelService는 전액 취소만 처리하고 있습니다. 부분취소 API가 추가되면 위 규칙을 바로 연결할 예정입니다. 문제 없을까요?
추가로, 동시성 정책은 “낙관적 락 실패 시 즉시 실패 + ERROR 로그”로 구현하겠습니다. 위 B 규칙과 A 기준만 확인되면 Payment/Cancel에 매니저 연동 및 테스트까지 바로 진행하겠습니다.

[Answer]
A) ii
B) iii = 최소 주문금액이 깨지는 순간에 복원해
C) 부분 취소는 현재 없는 상태라서 전액 취소만 처리해


# 장바구니 - 레거시 + AI 느끼기
## P1
장바구니 개념의 코드를 리팩토링해
먼저 병렬 작업이 가능하도록 적절한 작업 단위를 나눠서 작업 계획을 refactor/CART.MD 파일에 정리해

[File]
CartService.kt

## P2
[타깃 구조 - 수정사항]
- 이번엔 Service 조합이 필요 없기 때문에 CartAssembler 만들 필요 없어 계획 수정해

[WS-A 수정사항]
- CART-01: ProductRepository 말고 ProductFinder를 활용하도록 계획 수정해
- CART-02: 파라미터로 기존 클래스를 활용하도록 계획 수정해
- CART-03: 이 작업은 제외해

## P3
CART.MD 기준으로 리팩토링해

## P4
이 작업을 통해서 학습된 추가 가이드라인이 있다면 해당 내용을 .junie/guidelines.md에 반영해
기존 내용과 중복되지 않고 추가된 내용만 추가하거나 확장해서 수정해
추가하려는 내용과 관련된 내용이 기존 문서에 있다면 그 항목 밑에다 추가해

## P5
공유 장바구니 기능을 구현해
[요구사항]
- 공유 장바구니를 만들어서 링크를 공유
- 공유 장바구니는 여러 개가 될 수 있으며 삭제 가능
- 생성 후 7일이 지나면 공유 장바구니는 만료되어 신규 상품을 추가할 수 없음

[기존 기능에서의 수정 사항]
- 기존 장바구니 상품 추가 API(/v1/cart/items)에 공유 장바구니에 상품을 추가할 수 있어야 한다

[신규 API 추가 필요]
- 공유 장바구니 목록 조회
- 공유 장바구니 생성
- 공유 장바구니 삭제
- (링크 공유 받은 사람) 공유 장바구니 수락하기

[File]
CartController.kt
CartService.kt
CartItemManager.kt
CartReader.kt
CartItemEntity.kt

## P6
SharedCart에 대한 Entity를 추가하는 것보다 CartEntity를 추가하고 타입을 지정하면 어떨까?
* 문제점이나 다른 생각이 있다면 물어봐

[Question]
제안하신 방향(단일 Cart/CartItem 모델에 타입 필드로 개인/공유를 구분)으로 비교·평가 계획을 만들기 전에, 현재/목표 요구사항을 몇 가지 확인하고 싶습니다.
1)
공유 장바구니의 소유·참여 모델
• 공유 장바구니는 ‘소유자 1명 + 링크 수신 다수(익명/비로그인 포함 가능?)’ 구조인가요?
• 수신자가 장바구니에 담기/수정이 가능한가요, 아니면 오직 소유자만 수정하고 수신자는 수락(복사)만 하나요?
2)
만료/삭제 규칙
• 만료되면 “추가만 불가”인지, 수정/삭제도 모두 불가인지 확인 필요합니다.
• Soft Delete 정책은 기존과 동일(상태필드) 유지하면 될까요?
3)
링크 토큰과 조회 방식
• 현재 구현처럼 토큰(랜덤 문자열)로 접근하는 요구가 유지되나요? Cart 단일 테이블로 갈 경우, 토큰은 공유 타입에만 존재하는 nullable 컬럼로 괜찮을까요?
4)
기존 API/응답의 호환성
• 현 API를 유지(엔드포인트 동일)하면서 내부 구조만 바꾸는 것이 목표인가요?
• DB 마이그레이션 허용/필수 여부(데이터 이전 필요성)도 알려주세요.
5)
성능/조회 패턴
• 개인 장바구니 조회와 공유 장바구니 목록/조회가 한 테이블에서 함께 이뤄져도 괜찮을까요? 인덱스/파티셔닝 고려가 필요한지요?
6)
향후 확장성
• 공유 장바구니에 추가 속성(제목/메모/권한/이력 등) 계획이 있나요? 있다면 단일 테이블의 스파스 컬럼 증가가 허용될까요, 아니면 분리 유지가 안전할까요?
이 답변을 바탕으로, ‘단일 Cart 엔티티 + 타입 구분’ 옵션과 ‘분리 엔티티’ 옵션의 장단점, 마이그레이션 영향, 코드/쿼리 변화, 가이드라인 적합성까지 포함한 실행 계획(P6 전용 Plan)을 정리해서 드리겠습니다.

[Answer]
1-1) 소유자 1명 + 로그인 유저 다수
1-2) 수신자는 담기만 가능 + 수정 및 삭제 불가능
2-1) 만료되면 모든 작업 불가능
2-2) 유지
3-1) 랜덤 문자열 주소로 접근하는 것은 괜찮아 Cart 단일 테이블 유지를 기본으로 하되, 공유 매핑에 대한 별도 테이블은 만들어줘, 가급적 nullable은 사용하지 말아
4-1) 가능한 API는 유지하고 내부 구조를 위주로 작업해
4-2) DB 마이그레이션은 필요하면 진행할 거야
5-1) API 요청에 따라 Type 기반으로 조회를 분기하면 될 것 같아
6-1) 일단 지금은 계획 없어


# 주문 - 레거시 + AI 느끼기
## P1
주문 개념의 코드를 리팩토링해

[File]
OrderController.kt
OrderService.kt

## P2
- OrderService의 create 함수의 productFinder.find(...) 쓰는 부분이 기존 로직에서 변경된 것 같아 이 부분 확인하고 수정해, 수정 방향성을 잘 모르겠으면 다시 물어봐
- OrderReader.getPaidOrders를 getOrders로 바꾸고 state를 파라미터로 받도록 변경해

## P3
기존 함수를 수정하면 해당 함수를 사용하는 모든 곳에 영향도가 생기잖아
함수를 추가하는 쪽으로 해줘

## P4
- OrderReader.getOrders를 쓰면 되잖아  getPaidOrders를 왜 추가했어 제거해
- readOnly 트랜잭션 어노테이션이 꼭 필요한 것 아니면 쓰지 말아

## P5
create의 로직 중 products 검증에 대한 로직을 orderManager.create 안에 넣을 수 있지 않을까?
Service 클래스에선 비즈니스 로직을 느낄 수 있으면 좋겠어

## P6
이번 작업에서 알게 된 사항, 반복해서 수정 요청했던 사항에 대해 가이드라인에 기록해서 같은 실수를 하지 않도록 구성해

## P7
상품 옵션(ProductOption) 개념이 추가되어서 주문의 단위가 상품 옵션으로 변경되었어
이에 대한 주문 개념의 기준을 Product가 아니라 ProductOption이 되도록 수정을 진행해

[File]
OrderController.kt
OrderService.kt

## P8
- getOrder API의 OrderResponse에도 ProductOption 정보를 내리도록 해줘
- create API의 CreateOrderRequest에서 productOptionId와 quantity는 목록으로 올 수 있어, 이에 따라 구현을 변경해

## P9
"나 이거 사줘!"라는 기능의 추가 구현이 필요해
이 기능을 사용하면 주문 링크를 생성해서 다른 사용자가 내 주문서를 볼 수 있고 대신 결제해 줄 수 있어

- "나 이거 사줘!"를 사용한 주문에 대한 링크를 생성해, 해당 데이터 관리가 필요할 것 같아
    - 추후 기능 제거를 위해서 별도 테이블로 관리하는 게 좋을 것 같아
    - OrderInvite로 테이블 생성해

[File]
OrderController.kt
OrderService.kt


# 결제 - 레거시 + AI 느끼기
## P1
결제 개념의 코드를 리팩토링해

[File]
PaymentController.kt
PaymentService.kt

## P2
결제 개념의 코드를 리팩토링해
- PaymentService.createPayment는 PaymentCreator 컴포넌트로 묶어줘
- PaymentService.success의 PG API 호출 전까지는 PaymentManager의 validateForApprove 함수로 만들어
- PaymentService.success의 PG API 호출 이후 로직은 PaymentManager의 success 함수로 만들어
    - success 함수에서는 Order와 Payment를 다시 한번 조회하도록 해줘, 파라미터에 Entity를 주고받지 말아
    - pointHandler 처리까지 success 함수에서 처리해
- PaymentService.success의 TransactionHistory를 저장하는 부분은 TransactionHistoryAppender로 추출해
- PaymentService.fail은 PaymentManager의 validateForFail, TransactionHistoryAppender을 사용하게 바꿔

[File]
PaymentController.kt
PaymentService.kt

## P3
결제가 완료되면 결제된 상품을 장바구니에서 삭제하도록 구현해야 해
다양한 구현 방식과 전략이 있을 것 같은데 이것에 대해서 implementation/PAYMENT_CART_REMOVE.md 파일에 정리해

그럼 정리된 사항을 보고 어떤 전략으로 구현할지 결정하고 알려줄게

## P4
전략3과 유사하게 작업하는데 PaymentPostProcessor라는 걸 만들고 거기에서 처리하도록 해줘
대신 메인 로직이 아니니까 트랜잭션에서 벗어나서 @Async 기반으로 처리해도 될 것 같아

## P5
주문에 대해서 대신 결제하는 기능을 추가해
- OrderInvite에 등록된 주문은 다른 사용자가 대신 결제해 줄 수 있어
- 대신 결제 시에는 쿠폰이나 포인트를 사용할 수 없어

[File]
PaymentController.kt
PaymentService.kt
OrderInviteEntity.kt
OrderEntity.kt


# 취소 - 레거시 + AI 느끼기
## P1
취소 개념의 코드를 리팩토링해

[File]
CancelController.kt
CancelService.kt

## P2
취소 개념의 코드를 리팩토링해

PaymentService를 참고해서 결제 쪽과 처리 흐름과 패턴이 비슷하게 만들어

[File]
CancelController.kt
CancelService.kt
PaymentService.kt

## P3
부분 취소 기능을 구현해
아래 내용을 만족하는 부분 취소 테스트를 작성하고 예제 케이스에 대한 테스트가 모두 완벽히 통과시켜

[구현 기본 아이디어]
- 기존 전체 취소 기능을 그대로 두고 부분 취소에 대한 별도의 API를 추가하고, 기능 구현은 Cancel 컴포넌트에 별도 함수로 만들어줘
- OrderItem에서도 OrderState 상태를 관리해서 취소 상태를 알 수 있게 해줘
- 이런 흐름을 생각하고 있어
    - 해당 OrderItem 과 수량이 취소가 가능한지 validation 진행
    - PG에 전달할 취소 결제 금액을 계산
    - PG의 부분 취소 API 호출 (해당 부분은 CancelService와 동일하게 주석 처리 또는 mocking)
    - 부분 취소 처리 진행
- 취소의 최종 결과는 CancelEntity에 저장해
    - CancelType을 추가해서 전체 취소인지 부분 취소인지 기록해
    - 취소한 결제금액, 포인트 금액, 쿠폰 할인 금액을 기록해

[취소 금액 관리 전략]
- CancelBalance라는 테이블을 만들어서 취소 잔여 금액을 관리하면 좋을 것 같아
    - 해당 테이블에 취소 가능한 결제금액, 포인트 금액, 쿠폰 금액을 관리해
    - 취소된 금액도 대칭으로 관리해

[부분 취소 정책]
- 부분 취소가 진행된 주문 건은 전체 취소할 수 없어

- 부분 취소는 OrderItem 단위로 취소가 가능한 기능이야
    - OrderItem의 수량 단위 취소도 가능해
    - OrderItem의 취소 가능한 수량도 관리해

- 부분 취소는 아래 세 가지 재화에 대해 순차적으로 취소 처리하는 로직이 필요해
    - 세 가지 재화 : 실제 결제 금액, 쿠폰 할인 금액, 포인트 사용 금액

- 각각의 정책은 이렇게 되어있어
    - 포인트 사용 금액을 가장 마지막에 취소한다
    - 실제 결제 금액을 다음으로 취소한다
    - 취소 금액을 차감하던 중 남은 취소 금액이 쿠폰의 '최소 주문 금액' 보다 낮아지는 상황이라면 쿠폰에 대하여 취소를 진행한다
        - 이때에 고객은 취소에 대하여 돈을 받지 못하고 쿠폰을 돌려받는다

[취소 예제]
[Case1 - Point 결제]
[Payment]
- orderAmount : 30,000
- usedPoint : 15,000
- paidAmount : 15,000
  [Order]
- OrderItem-1 : 10,000
- OrderItem-2 : 10,000
- OrderItem-3 : 10,000

[Cancel]
- OrderItem-1-Cancel:
    - cancelPaidAmount : 10,000
    - remainPaidAmount : 5,000
    - cancelPointAmount : 0
    - remainPointAmount : 15,000
- OrderItem-2-Cancel:
    - cancelPaidAmount : 5,000
    - remainPaidAmount : 0
    - cancelPointAmount : 5,000
    - remainPointAmount : 10,000
- OrderItem-3-Cancel:
    - cancelPaidAmount : 0
    - remainPaidAmount : 0
    - cancelPointAmount : 10,000
    - remainPointAmount : 0

[Case2 - 쿠폰 결제]
[Payment]
- orderAmount : 50,000
- couponDiscount : 10,000
- paidAmount : 40,000
  [Coupon]
- couponMinimumOrderAmount : 35,000
- couponDiscount : 10,000
  [Order]
- OrderItem-1 : 10,000
- OrderItem-2 : 10,000
- OrderItem-3 : 10,000
- OrderItem-4 : 10,000
- OrderItem-5 : 10,000

[Cancel]
- OrderItem-1-Cancel:
    - cancelPaidAmount : 10,000
    - remainPaidAmount : 30,000
    - cancelCouponDiscountAmount : 0
    - remainCouponDiscountAmount : 10,000
- OrderItem-2-Cancel:
    - cancelPaidAmount : 0
    - remainPaidAmount : 30,000
    - cancelCouponDiscountAmount : 10,000
    - remainCouponDiscountAmount : 0
        - CouponBroken: Coupon Restore
- OrderItem-3-Cancel:
    - cancelPaidAmount : 10,000
    - remainPaidAmount : 20,000
    - cancelCouponDiscountAmount : 0
    - remainCouponDiscountAmount : 0
- OrderItem-4-Cancel:
    - cancelPaidAmount : 10,000
    - remainPaidAmount : 10,000
    - cancelCouponDiscountAmount : 0
    - remainCouponDiscountAmount : 0
- OrderItem-5-Cancel:
    - cancelPaidAmount : 10,000
    - remainPaidAmount : 0
    - cancelCouponDiscountAmount : 0
    - remainCouponDiscountAmount : 0

[Case3 - 쿠폰 결제]
[Payment]
- orderAmount : 60,000
- usedPoint : 15,000
- couponDiscount : 15,000
- paidAmount : 30,000
  [Coupon]
- couponMinimumOrderAmount : 35,000
- couponDiscount : 15,000
  [Order]
- OrderItem-1 : 10,000
- OrderItem-2 : 10,000
- OrderItem-3 : 10,000
- OrderItem-4 : 10,000
- OrderItem-5 : 10,000
- OrderItem-6 : 10,000

[Cancel]
- OrderItem-1-Cancel:
    - cancelPaidAmount : 10,000
    - remainPaidAmount : 20,000
    - cancelPointAmount : 0
    - remainPointAmount : 15,000
    - cancelCouponDiscountAmount : 0
    - remainCouponDiscountAmount : 15,000
- OrderItem-2-Cancel:
    - cancelPaidAmount : 10,000
    - remainPaidAmount : 10,000
    - cancelPointAmount : 0
    - remainPointAmount : 15,000
    - cancelCouponDiscountAmount : 0
    - remainCouponDiscountAmount : 15,000
- OrderItem-3-Cancel:
    - cancelPaidAmount : 0
    - remainPaidAmount : 10,000
    - cancelPointAmount : 0
    - remainPointAmount : 15,000
    - cancelCouponDiscountAmount : 10,000
    - remainCouponDiscountAmount : 5,000
- OrderItem-4-Cancel:
    - cancelPaidAmount : 5,000
    - remainPaidAmount : 5,000
    - cancelPointAmount : 0
    - remainPointAmount : 15,000
    - cancelCouponDiscountAmount : 5,000
    - remainCouponDiscountAmount : 0
- OrderItem-5-Cancel:
    - cancelPaidAmount : 5,000
    - remainPaidAmount : 0
    - cancelPointAmount : 5,000
    - remainPointAmount : 10,000
    - cancelCouponDiscountAmount : 0
    - remainCouponDiscountAmount : 0
- OrderItem-6-Cancel:
    - cancelPaidAmount : 0
    - remainPaidAmount : 0
    - cancelPointAmount : 10,000
    - remainPointAmount : 0
    - cancelCouponDiscountAmount : 0
    - remainCouponDiscountAmount : 0

[File]
CancelController.kt
CancelService.kt
CancelProcessor.kt
CancelValidator.kt


## P4
- CancelService.partialCancel의 로직의 흐름이 아래와 같이 변경해
    - cancelValidator.validate(...)
        - 부분 취소 validate 함수를 별도로 분리해
    - cancelCalculator.calculate(...)
        - 이 부분에서 취소 금액을 계산한 결과를 return 해
    - partialCancel.partialCancel
- OrderState에 부분 취소 상태를 추가해
    - OrderItem의 일부 수량이 취소되면 부분 취소 상태가 되어야 해
    - 모든 수량이 취소되면 CANCELED가 되어야 해
- CancelEntity의 canceledAmount와 canceledPaidAmount는 중복이라서 canceledPaidAmount를 삭제해

## P5
- 부분 취소 금액을 계산하는 CancelCalculator 클래스를 구현해
    - 함수 형태는 calculatePartial(orderKey: String, orderItemId: Long, cancelQuantity: Long) 이렇게 구성해
    - 반환 값으로는 CancelCalculateResult라는 클래스를 만들어
        - CancelCalculateResult의 필드는 취소 처리해야 하는 금액인 paidAmount, couponAmount, pointAmount 세 개의 필드를 갖게 해
- 아래 예제 3개에 대한 CancelCalculator.calculatePartial의 계산 로직 검증 테스트 케이스를 만들고 테스트가 모두 성공하도록 해

[부분 취소 계산 정책]
- 부분 취소는 아래 세 가지 재화에 대해 순차적으로 취소 처리하는 로직이 필요해
    - 세 가지 재화 : 실제 결제 금액, 쿠폰 할인 금액, 포인트 사용 금액

- 각각의 정책은 이렇게 되어있어
    - 포인트 사용 금액을 가장 마지막에 취소한다
    - 실제 결제 금액을 다음으로 취소한다
    - 취소 금액을 차감하던 중 남은 취소 금액이 쿠폰의 '최소 주문 금액' 보다 낮아지는 상황이라면 쿠폰에 대하여 취소를 진행한다
        - 이때에 고객은 취소에 대하여 돈을 받지 못하고 쿠폰을 돌려받는다


[부분 취소 예제]
[Case1 - Point 결제]
[Payment]
- orderAmount : 30,000
- usedPoint : 15,000
- paidAmount : 15,000
  [Order]
- OrderItem-1 : 10,000
- OrderItem-2 : 10,000
- OrderItem-3 : 10,000

[Cancel]
- OrderItem-1-Cancel:
    - cancelPaidAmount : 10,000
    - remainPaidAmount : 5,000
    - cancelPointAmount : 0
    - remainPointAmount : 15,000
- OrderItem-2-Cancel:
    - cancelPaidAmount : 5,000
    - remainPaidAmount : 0
    - cancelPointAmount : 5,000
    - remainPointAmount : 10,000
- OrderItem-3-Cancel:
    - cancelPaidAmount : 0
    - remainPaidAmount : 0
    - cancelPointAmount : 10,000
    - remainPointAmount : 0

[Case2 - 쿠폰 결제]
[Payment]
- orderAmount : 50,000
- couponDiscount : 10,000
- paidAmount : 40,000
  [Coupon]
- couponMinimumOrderAmount : 35,000
- couponDiscount : 10,000
  [Order]
- OrderItem-1 : 10,000
- OrderItem-2 : 10,000
- OrderItem-3 : 10,000
- OrderItem-4 : 10,000
- OrderItem-5 : 10,000

[Cancel]
- OrderItem-1-Cancel:
    - cancelPaidAmount : 10,000
    - remainPaidAmount : 30,000
    - cancelCouponDiscountAmount : 0
    - remainCouponDiscountAmount : 10,000
- OrderItem-2-Cancel:
    - cancelPaidAmount : 0
    - remainPaidAmount : 30,000
    - cancelCouponDiscountAmount : 10,000
    - remainCouponDiscountAmount : 0
        - CouponBroken: Coupon Restore
- OrderItem-3-Cancel:
    - cancelPaidAmount : 10,000
    - remainPaidAmount : 20,000
    - cancelCouponDiscountAmount : 0
    - remainCouponDiscountAmount : 0
- OrderItem-4-Cancel:
    - cancelPaidAmount : 10,000
    - remainPaidAmount : 10,000
    - cancelCouponDiscountAmount : 0
    - remainCouponDiscountAmount : 0
- OrderItem-5-Cancel:
    - cancelPaidAmount : 10,000
    - remainPaidAmount : 0
    - cancelCouponDiscountAmount : 0
    - remainCouponDiscountAmount : 0

[Case3 - 쿠폰 결제]
[Payment]
- orderAmount : 60,000
- usedPoint : 15,000
- couponDiscount : 15,000
- paidAmount : 30,000
  [Coupon]
- couponMinimumOrderAmount : 35,000
- couponDiscount : 15,000
  [Order]
- OrderItem-1 : 10,000
- OrderItem-2 : 10,000
- OrderItem-3 : 10,000
- OrderItem-4 : 10,000
- OrderItem-5 : 10,000
- OrderItem-6 : 10,000

[Cancel]
- OrderItem-1-Cancel:
    - cancelPaidAmount : 10,000
    - remainPaidAmount : 20,000
    - cancelPointAmount : 0
    - remainPointAmount : 15,000
    - cancelCouponDiscountAmount : 0
    - remainCouponDiscountAmount : 15,000
- OrderItem-2-Cancel:
    - cancelPaidAmount : 10,000
    - remainPaidAmount : 10,000
    - cancelPointAmount : 0
    - remainPointAmount : 15,000
    - cancelCouponDiscountAmount : 0
    - remainCouponDiscountAmount : 15,000
- OrderItem-3-Cancel:
    - cancelPaidAmount : 0
    - remainPaidAmount : 10,000
    - cancelPointAmount : 0
    - remainPointAmount : 15,000
    - cancelCouponDiscountAmount : 10,000
    - remainCouponDiscountAmount : 5,000
- OrderItem-4-Cancel:
    - cancelPaidAmount : 5,000
    - remainPaidAmount : 5,000
    - cancelPointAmount : 0
    - remainPointAmount : 15,000
    - cancelCouponDiscountAmount : 5,000
    - remainCouponDiscountAmount : 0
- OrderItem-5-Cancel:
    - cancelPaidAmount : 5,000
    - remainPaidAmount : 0
    - cancelPointAmount : 5,000
    - remainPointAmount : 10,000
    - cancelCouponDiscountAmount : 0
    - remainCouponDiscountAmount : 0
- OrderItem-6-Cancel:
    - cancelPaidAmount : 0
    - remainPaidAmount : 0
    - cancelPointAmount : 10,000
    - remainPointAmount : 0
    - cancelCouponDiscountAmount : 0
    - remainCouponDiscountAmount : 0

## P6
CancelCalculateResult에 쿠폰이 `이번 취소로 복원해야 하는 것` 인지 체크하는 상태 필드 추가해

## P7
- cancelRepository.findAllByPaymentId(payment.id) 사용하는 대신 CancelBalanceEntity를 추가해서 아래 금액을 관리하도록 해줘
    - 해당 테이블에 취소 가능한 결제금액, 포인트 금액, 쿠폰 금액을 관리해
    - 취소된 금액도 대칭으로 관리해

## P8
- CancelBalanceEntity에 OrderId를 키로 변경해
- version을 추가하고 낙관적락을 구성해

## P9
CancelService에 OrderItemId, quantity으로 취소를 할 수 있는 partialCancel 함수를 구현해

[구현 처리 흐름]
- CancelValidator.validatePartial(...)
- CancelCalculator.calculatePartial(...)
- CancelProcessor.partialCancel(...)

## P10
- CancelValidator.validatePartial에서 orderItem을 찾을 때 orderItemId를 바로 사용해
- PartialCancelRequest에서 partialCancel으로 전달할 때 PartialCancelAction 을 만들어서 전달해
- CancelEntity에 CancelType을 추가해서 전체 취소, 부분 취소 나눠서 기록해
- OrderItemEntity에 OrderState를 추가해, 그리고 수량 일부만 취소된 상태면 부분 취소로 기록해 주고, 전체 수량이 취소되었으면 전체 취소로 기록해

## P11
PaymentProcessor.sucess 호출 시 결제가 성공 된 것이기 때문에 OrderItemEntity의 state를 PAID 상태로 변경해

## P12
- CancelEntity, TransactionHistoryEntity에 취소 결제 금액, 취소 포인트 금액, 취소 쿠폰 금액에 대한 필드를 다 추가해서 기록해
    - 기존 CancelEntity.cancledAmount, TransactionHistoryEntity.amount는 취소 결제 금액에 해당해서 취소 포인트 금액, 취소 쿠폰 금액 두 개만 추가해
    - TransactionHistoryEntity에 Type에 PARTIAL_CANCELD 추가해

## P13
CancelCalculatorTest에서 검증한 3개의 예제에 대하여 CancelService 기준으로 부분 취소에 대한 통합 테스트를 작성해서 모두 통과시켜줘

[부분 취소 예제]
[Case1 - Point 결제]
[Payment]
- orderAmount : 30,000
- usedPoint : 15,000
- paidAmount : 15,000
  [Order]
- OrderItem-1 : 10,000
- OrderItem-2 : 10,000
- OrderItem-3 : 10,000

[Cancel]
- OrderItem-1-Cancel:
    - cancelPaidAmount : 10,000
    - remainPaidAmount : 5,000
    - cancelPointAmount : 0
    - remainPointAmount : 15,000
- OrderItem-2-Cancel:
    - cancelPaidAmount : 5,000
    - remainPaidAmount : 0
    - cancelPointAmount : 5,000
    - remainPointAmount : 10,000
- OrderItem-3-Cancel:
    - cancelPaidAmount : 0
    - remainPaidAmount : 0
    - cancelPointAmount : 10,000
    - remainPointAmount : 0

[Case2 - 쿠폰 결제]
[Payment]
- orderAmount : 50,000
- couponDiscount : 10,000
- paidAmount : 40,000
  [Coupon]
- couponMinimumOrderAmount : 35,000
- couponDiscount : 10,000
  [Order]
- OrderItem-1 : 10,000
- OrderItem-2 : 10,000
- OrderItem-3 : 10,000
- OrderItem-4 : 10,000
- OrderItem-5 : 10,000

[Cancel]
- OrderItem-1-Cancel:
    - cancelPaidAmount : 10,000
    - remainPaidAmount : 30,000
    - cancelCouponDiscountAmount : 0
    - remainCouponDiscountAmount : 10,000
- OrderItem-2-Cancel:
    - cancelPaidAmount : 0
    - remainPaidAmount : 30,000
    - cancelCouponDiscountAmount : 10,000
    - remainCouponDiscountAmount : 0
        - CouponBroken: Coupon Restore
- OrderItem-3-Cancel:
    - cancelPaidAmount : 10,000
    - remainPaidAmount : 20,000
    - cancelCouponDiscountAmount : 0
    - remainCouponDiscountAmount : 0
- OrderItem-4-Cancel:
    - cancelPaidAmount : 10,000
    - remainPaidAmount : 10,000
    - cancelCouponDiscountAmount : 0
    - remainCouponDiscountAmount : 0
- OrderItem-5-Cancel:
    - cancelPaidAmount : 10,000
    - remainPaidAmount : 0
    - cancelCouponDiscountAmount : 0
    - remainCouponDiscountAmount : 0

[Case3 - 쿠폰 결제]
[Payment]
- orderAmount : 60,000
- usedPoint : 15,000
- couponDiscount : 15,000
- paidAmount : 30,000
  [Coupon]
- couponMinimumOrderAmount : 35,000
- couponDiscount : 15,000
  [Order]
- OrderItem-1 : 10,000
- OrderItem-2 : 10,000
- OrderItem-3 : 10,000
- OrderItem-4 : 10,000
- OrderItem-5 : 10,000
- OrderItem-6 : 10,000

[Cancel]
- OrderItem-1-Cancel:
    - cancelPaidAmount : 10,000
    - remainPaidAmount : 20,000
    - cancelPointAmount : 0
    - remainPointAmount : 15,000
    - cancelCouponDiscountAmount : 0
    - remainCouponDiscountAmount : 15,000
- OrderItem-2-Cancel:
    - cancelPaidAmount : 10,000
    - remainPaidAmount : 10,000
    - cancelPointAmount : 0
    - remainPointAmount : 15,000
    - cancelCouponDiscountAmount : 0
    - remainCouponDiscountAmount : 15,000
- OrderItem-3-Cancel:
    - cancelPaidAmount : 0
    - remainPaidAmount : 10,000
    - cancelPointAmount : 0
    - remainPointAmount : 15,000
    - cancelCouponDiscountAmount : 10,000
    - remainCouponDiscountAmount : 5,000
- OrderItem-4-Cancel:
    - cancelPaidAmount : 5,000
    - remainPaidAmount : 5,000
    - cancelPointAmount : 0
    - remainPointAmount : 15,000
    - cancelCouponDiscountAmount : 5,000
    - remainCouponDiscountAmount : 0
- OrderItem-5-Cancel:
    - cancelPaidAmount : 5,000
    - remainPaidAmount : 0
    - cancelPointAmount : 5,000
    - remainPointAmount : 10,000
    - cancelCouponDiscountAmount : 0
    - remainCouponDiscountAmount : 0
- OrderItem-6-Cancel:
    - cancelPaidAmount : 0
    - remainPaidAmount : 0
    - cancelPointAmount : 10,000
    - remainPointAmount : 0
    - cancelCouponDiscountAmount : 0
    - remainCouponDiscountAmount : 0


# 정산 - 레거시 + AI 느끼기
## P1
정산 개념의 코드를 리팩토링해줘
- 파라미터나 반환 값에 Entity를 사용하지 말고 필요한 경우 사용하는 필드만 추출해서 클래스를 만들어줘
    - 필드만 추출한 클래스 명의 prefix는 Settlement로 해줘

## P2
- 정산 개념에 부분 취소가 추가됐는데 부분 취소에 대한 변경 사항을 정산 개념에 반영해
    - 기존엔 전체 취소밖에 없었어
    - 이제 부분 취소가 추가되어서 그에 대한 계산이 필요해

## P3
- SettlementTargetManager의 targetAmount를 개선해
    - payment는 transactionType, transactionIdMap을 제거하고 양수로 처리해
    - cancel은 음수로 처리해
    - 부분 취소는  canceledPaidAmount는 실 결제 금액에 대한 취소 금액이라 정산 시에는 totalPrice를 그대로 쓰는 게 맞아

## P4
- SettlementTargetManager의 부분 취소 기능이 cancels를 flatmap으로 순회하면서 처리하고 있는데 건별로 DB 조회하는 패턴을 개선해
    - 함수 내부에서 로직 처리 순서를 전체 취소 처리 후 부분 취소 처리하도록 나눠서 정리해

## P5
- processAllCancels 은 로직 처리 방식이 논리적으로 processPayments 랑 같아, processAllCancels을 processPayments과 동일한 로직 처리 방식으로 수정해
    - targetAmount 부분만 음수인 것 빼고 동일한 로직이야

## P6
SettlementService의 loadTargets, transfer도 calculate처럼 적절한 Logic Layer 컴포넌트를 추가해서 정리해

## P7
최근 한 달 기준 정산 금액에 따라 정산수수료를 다르게 적용하려고 해, 이에 대한 작업을 진행해
더 필요한 정보가 있으면 물어봐

[슬라이딩 정산 기본 정책]
- (현재) 기본 정산 수수료 10%
- 최근 매출이 10,000,000 초과면 정산 수수료 8%
- 최근 매출이 100,000,000 초과면 정산 수수료 5%

[File]
SettlementService.kt
SettlementReader.kt
SettlementManager.kt

## P8
- 한 달 매출액 가져오는 부분이 반복문 안에서 실행되고 있는데 일괄 가져와서 처리해
- 매출액 계산은 정산 상태가 완료인 것 만 계산해

[File]
SettlementManager.kt
SettlementCalculator.kt

## P9
- 가맹점별 정산 주기를 다르게 해달라는 요구사항을 구현해
    - 가맹점 정산 주기는 MerchantEntity에 추가해
    - 가맹점 정산 주기는 1-30일 사이로 선택할 수 있어
        - 정산 주기가 5면 5일마다, 7이면 7일마다 정산을 해준다는 의미야
* 추가 사항에 대해서 결정하기 모호한 게 있다면 질문해

[File]
SettlementBatchController.kt
SettlementService.kt

## P10
- 가맹점별 정산 주기를 다르게 해달라는 요구사항을 구현해
    - 가맹점 정산 주기는 MerchantEntity에 추가해
    - 가맹점 정산 주기는 1-30일 사이로 선택할 수 있어
        - 정산 주기가 5면 5일마다, 7이면 7일마다 정산을 해준다는 의미야
- 기존 코드의 맥락을 이해해 보면 SettlementTransferProcessor에서 Merchant의 정산 주기가 되었을 때 정산에 대한 이체를 진행하도록 하면 될 것 같아
    - 최종 정산 데이터를 읽은 후 해당 Merchant의 정산 주기가 해당된 날에만 이체를 진행하면 될 것 같아

[기존 코드 맥락]
- SettlementService
    - loadTargets : 정산 대상을 수집하는 기능
    - calculate : 수집된 정산 대상에 수수료를 적용한 최종 정산 데이터를 만드는 기능
    - transfer : 최종 정산 데이터를 기반으로 가맹점에 실제 정산을 진행하는 기능
* 각 함수가 별도 API 배치에서 수행되는 방식

[File]
SettlementBatchController.kt
SettlementService.kt
SettlementTransferProcessor.kt

