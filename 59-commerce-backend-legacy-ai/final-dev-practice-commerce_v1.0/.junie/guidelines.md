# 프로젝트 기본 규칙 

## 일반 사항
- 해당 프로젝트는 DDD 기반이 아니다
- 해당 프로젝트는 아직 규모가 작기 때문에 헥사고날을 사용하지 않는다
- 모호한 `도메인` 이라는 말 대신 `개념` 이라는 말을 사용한다
  - 도메인 객체 -> 개념 객체
  - 도메인 영역 -> 개념 영역


## 아키텍처 기본 규칙
- 큰 틀의 계층은 3개의 계층이나, 명시적인 분리를 위해 4개의 영역으로 관리한다
  - 1. Presentation Layer (Controller)
  - 2. Business Layer (Service)
  - 3. Logic Layer (*Finder, *Reader ... etc)
  - 4. Data Access Layer (*Repository, *Client ... etc)

* 2,3 계층은 작업자들의 혼돈을 줄이기 위해 명시적으로 분리한 임의의 영역이다 

[4가지 계층 규칙]
- 규칙1. 레이어는 위에서 아래로 순방향으로만 참조 되어야한다 
- 규칙2. 레이어는 참조 방향이 역류 되지 않아야한다
- 규칙3. 레이어의 참조가 하위 레이어를 건너 뛰지 않아야한다
- 규칙4. 동일 레이어간에는 서로 참조하지 않아야한다
  - Logic Layer는 예외적으로 서로 참조가 가능하다
  - 이 규칙은 Logic Layer 클래스들의 재사용성을 늘리고 협력이 가능한 높은 완결성의 도구 클래스들을 더 많이 만들게 합니다.

위의 규칙은 가장 기본적인 느슨한 규칙이며 나머지 상세사항은 상식적으로 판단하며 필요한 내용은 추후 정의한다


## 아키텍처 및 계층 분리 전략

[Presentation Layer]
- Presentation Layer 는 외부 API Request 를 받아서 충분히 검증한 후 Business Layer 로 전달한다
  - 그에 대한 `개념 객체` 변환 함수는 `.to***(), .to***(xx, cc)` 으로 명명한다
    - `to***` 함수는 파라미터를 받거나 받지 않을 수 있다
    - to***(...) 함수는 내부에서 예외를 시킬 수 있으며, 길이 검증, 빈 값 검증 등등의 역할을 할 수 있다
- 작업자들의 혼란을 막기위해 Presentation Layer 에는 비즈니스 로직을 담지 않는다

[Business Layer]
- Business Layer 는 Logic Layer 의 컴포넌트를 조합하여 로직을 처리한다 - AI 제안 + 팀 논의 사항
- 비즈니스의 흐름이 잘 보이도록 구현한다


## 인증 및 인가에 대한 전략
- 이 프로젝트 서버는 Internal Network 에 존재한다
- 외부 Gateway 를 통해 인증 및 인가가 완료 된 상태로 API 요청이 들어온다
- Controller 의 `User` 클래스는 `UserArgumentResolver` 를 통해 인증이 완료 된 User의 정보다
  - 인증이 완료 되어있고 신뢰 할 수 있는 상태이다


## 레거시 개선 및 신규 작성 코드에 대한 규칙

### Logic Layer 의 Data Access Layer 접근
- 생산성과 효율을 위해 Logic Layer 에서 JPA Entity, Repository 의 직접적인 접근을 허용한다
- 함수의 parameter/return 시에 Entity 를 반환하지 않는다 

### Controller의 Service 조합 전략 - AI 제안 + 팀 논의 사항
- Controller가 Service 를 직접 조합하지 않는다
- Service 조합을 위한 `Assembler` 클래스를 구성한다 
- Presentation Layer(Controller) 는 Web,APP UI 요구사항에 따라 요청을 처리해야하는 경우가 많으므로 비교적 중요한 영역이라고 보지 않는다

### Assembler 클래스 구현 규칙
- Assembler 는 요구사항 중 UI를 위한 처리나, 비즈니스 로직이 아닌 부분을 해소하기 위해 사용한다
- 패키지를 `io.dodn.commerce.core.api.assembler` 로 명명한다
- 추후 오용을 없애기 위해 Facade 나 UseCase 를 명시적으로 사용하지 않는다
- Assembler는 Presentation Layer에 존재한다
- Assembler는 *Service 클래스만을 참조 가능하다
- Assembler는 *Finder, *Reader, *Manager 등 Logic Layer의 클래스를 절대 참조할 수 없다
- Assembler는 JPA Repository 등 Data Access Layer 클래스를 절대 참조 할 수 없다
- Assembler는 “서비스 조합이 필요한 경우에만” 도입한다. 요구사항상 조합이 불필요한 경우 미도입을 허용한다.
- 도입하지 않더라도 기존 제약(위치: Presentation Layer, 참조: Service만 허용)은 유지된다.

### 클래스 네이밍 규칙
- Business Layer = *Service
- Logic Layer = Handler, Finder, Manager, Reader 등 행위 기반의 명명을 사용한다
* 절대적인 규칙은 아니지만 가급적 위의 명명 규칙을 사용한다

### @Transactional 적용 규칙
- Service에는 @Transactional을 사용하지 않는다(기존 규칙을 구체화).
- @Transactional은 Logic Layer에만 사용한다
- 읽기 함수는 필요하지 않으면 @Transactional을 사용하지 않는다 
- 쓰기 함수더라도 @Transactional 이 필요 없다면 사용하지 않는다
  - 단일 JPA Entity 의 *Repository.save() 등..
- 비즈니스적으로 트랜잭션이 묶이지 않아도 되는 경우에 상식적으로 판단하여 컴포넌트를 제안한다

### Entity <-> 개념 객체 변환 규칙 - AI 제안 + 팀 논의 사항
- Logic Layer에서 return 시에 Entity 를 반환하지 않는다 

### API Response DTO의 변환 규칙
- 로직이 필요한 변환의 경우 `*Response.of(...)` 함수를 사용한다
  - 복수 파라미터가 들어갈 수 있다
  - 해당 함수는 개념 객체를 활용하여 API 스펙을 맞추기위한 Converter 역할을 하고있다

### 컨트롤러 패키지 위치 규칙
- v1 API: `core.api.controller.v1` 패키지
- 배치 API: `core.api.controller.batch` 패키지

* 배치 시스템은 추후 분리 예정이므로 현재 패키지를 유지한다

### API URI 규칙
- REST API를 가능한 따르나 무조건 준수하지 않는다
- 단수, 동사 등을 사용 할 수 있다
- 의미가 더 잘 전달 되고 클라이언트 개발자들이 이해하기 쉬운 것을 규칙으로 한다

### 하드코딩 및 매직 넘버/문자열 규칙
- 가급적 하드코딩 하지 않고 모두 Enum or Object 으로 추출한다

### 엔티티 클래스와 개념 클래스 책임 규칙
- 두 클래스 모두 비즈니스 로직과 영속성에 대한 역할을 맡을 수 있다

### 테스트 규칙
- 모든 테스트는 생성자 주입을 받는다

### Component Annotation 규칙
- Business Layer = @Service
- Logic Layer = @Component

### API 응답 타입의 일관성 규칙
- 응답이 필요 없는 경우 `ApiResponse<Any>`
- 그 외에는 상황에 맞춰 단일 응답과 목록 응답을 사용 할 수 있다

### 개념 객체 패키지 규칙 - 팀 논의 사항
- `io.dodn.commerce.core.domain` 하위 패키지는 굳이 상세하게 나누지 않는다
  - 다만 클래스가 너무 많은 경우 개념 객체의 응집도를 유지하며 패키지 생성을 제안 할 수 있다 

### 상수 Enum 규칙
- 요구 사항 중 고정 된 값인 상수(날짜, 문자, 숫자 등)은 Enum 으로 구성한다
- 파일 추가 시 가까운 패키지에 추가한다

### Projection 클래스 추가 규칙
- 별도 파일로 분리한다

### 미사용 함수 및 의존성 주입 클래스 규칙
- 작업 후 클래스의 미사용 함수는 제거한다
- 작업 후 클래스의 미사용 의존성 주입 클래스는 제거한다

### 개념의 로직 클래스 우선 사용 원칙
- 상위/동일 Logic에서 “타 개념” 조회가 필요한 경우 Repository에 직접 의존하기보다 해당 개념의 로직 클래스를 우선 활용한다.
- 자기 개념의 영속 작업은 Repository 직접 접근을 허용한다(기존 생산성 규칙 유지).
 - Finder도 Soft Delete 정책을 기본 준수한다: 비활성/삭제 데이터는 조회 결과에서 제외하거나 명시적 옵션으로만 포함한다.

### Repository 시그니처 변경 원칙(병렬 작업 친화)
- 기존 메서드 시그니처 변경은 지양한다. 변경 필요 시 “신규 메서드 추가”로 대응한다.
- 대규모 병렬 작업 시 공용 시그니처/타입의 동결을 우선하고, 변경은 별도 이터레이션으로 이관한다.
- 영향도 최소화: 이미 다른 계층에서 사용 중인 Logic Layer의 함수를 수정할 때는 신규 함수를 추가하여 기존 호출부의 영향도를 없애는 것을 우선 검토한다.

### Soft Delete 정책(명시)
- `EntityStatus` 기반 상태 전이를 사용한다. `BaseEntity.active/delete/isActive/isDeleted`를 일관되게 활용한다.
- 조회 시 비활성/삭제 데이터는 비즈니스 규칙에 따라 필터링한다(Reader에서 우선 적용).
 - 재활성화 원칙: Soft Delete 대상의 재활성화가 필요할 경우 Manager 책임 하에 비즈니스 규칙을 검증한 뒤 `active()`를 호출한다.

### 리팩토링 전환 원칙(서비스 API 안정성)
- Logic Layer(Reader/Manager 등) 도입을 통한 내부 구조 개선 시, 외부 공개 `*Service`의 퍼블릭 시그니처는 가능하면 유지한다.
- 전환 과정에서는 `*Service`가 새 Logic 컴포넌트로 위임하도록 구현하고, 호출자(Controller/Assembler)의 변경을 최소화한다.
- 위임 후 정리: 모든 호출부가 새 Logic 컴포넌트를 직접 사용하게 된 경우에만 구형 함수를 제거하거나 정리한다.

### Data Access 성능 점검(확장)
- 대량 ID 기반 조회 사용 시(in-clause) 인덱스 상태와 in-list 크기 한계를 점검한다.
- 기존 Repository 시그니처 변경이 필요한 경우 지양하고, 필요 시 성능 목적의 “신규 메서드 추가”로 대응한다(상위 규칙 일관).

### DB 조회 성능을 위한 기본 지침
- 반복문 안에서 DB를 조회하는 구현보다는 가급적 반복문 밖에서 일괄 조회 후 사용하는 식으로 구현한다.
