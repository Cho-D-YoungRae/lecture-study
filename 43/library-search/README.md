# Library Search API

## 기능 요구사항

- 도서 검색 기능 
  - 도서 제목으로 도서를 검색하여 도서 정보를 제공해야 한다. 
  - 검색 결과는 페이징 형태로 제공되어야 한다. 
- 도서 검색 통계 기능 
  - 사용자들이 검색한 도서에 대한 검색어 횟수를 일별로 제공해야한다. 
  - 사용자들이 검색한 도서 순위를 상위 5개를 제공해야한다.

## 작업 목록

> EPIC: LS-0

- LS-1: 규모 추정 & 시스템 디자인
- LS-2: 요구사항 작성
- LS-3: 멀티모듈 구성
- LS-4: 외부 API 연동
- LS-5: api 서버 구현
- LS-6: 데이터베이스 연결
- LS-7: 검택 통계 기능 구현
- LS-8: 문서화
- LS-9: 고가용성을 위한 설계

## 강의 정리

FeignClient 사용

- 시간이 부족하기 때문에 빠르게 사용할 수 있는 것으로

JpaRepository.saveAndFlush DataJpaTest 시

- saveAndFlush 사용
  - 명시적으로 쿼리 날리기 위해
- 저장하면 기본적으로 영속성 컨텍스트에 저장되고 그냥 조회하면 쿼리가 날아가지 않고 컨텍스트 안의 엔티티가 조회될 수 있음
  - EntityManager.clear() 를 해서 영속성 컨텍스트를 비워주면 쿼리가 날아감

Circuit Breaker

- 예를 들어 A, B, C 에서 데이터를 조회해서 취합해 보여줄 경우
  - B를 제외한 A, C 라도 보여주는게 더 나을 수 있음
  - B가 정상으로 돌아올 때까지 호출하지 않음
- 일부 시스템의 장애가 전체 시스템에 영향을 주지 않도록
- Sliding window
  - count based, time based
  - 대표적으로 3가지 상태 존재
    - CLOSED
      - 평상시, 정상 
    - OPEN
      - 장애 발생
      - 실패율이 임계값 넘으면 OPEN 상태로 전환
    - HALF_OPEN
      - OPEN 상태에서 일정 시간 지나면 HALF_OPEN 상태로 전환. 바로 CLOSED로 전환 X
      - 설정된 수의 요청을 허용을 해서 외부 시스템이 사용 가능한지 확인
      - 정상으로 돌아오면 CLOSED로 전환
      - 장애가 발생하면 OPEN으로 전환
- 대표적인 설정
  - slidingWindowType
    - count based, time based
    - 기본: count based
  - slidingWindowSize
    - default: 100
  - minimumNumberOfCalls
    - 오류를 확인하기 위한 최소 호출 수
  - waitDurationOpenState
    - open -> half open 상태로 전환되기까지 대기 시간
  - permittedNumberOfCallsInHalfOpenState
    - half open 상태에서 허용되는 요청 수
    - default: 10
  - maxWaitDurationInHalfOpenState
    - half open 상태에서 대기하는 시간
  - ignoreExceptions
    - 기본 적으로 모든 예외를 실패로 간주하는데, 특정 예외는 무시할 수 있음

이벤트 발행

- 외부 API 호출 1초 + 통계 저장 1초 = 2초
- 통계 저장은 큰 관심사가 아니므로 사용자 입장에서 기다릴 필요 없음 -> 로직 격리
- 사용 이유
  - 비동기
  - 확장성: 구성 요소 간의 결합도를 낮춤
- 장점
  - 높은 응답성
  - 장애 격리
  - 복구 용이성
    - 이벤트 로그를 통해서 시스템 상태를 재구성할 수 있음
- 단점
  - 복잡성
    - 설계랑 구현이 상대적으로 복잡함
    - 이벤트 브로커 등의 인프라 추가 관리해야할 수 있음
  - 디버깅
  - 일관성 보장 어려움
    - 이벤트 순서를 보장하고 데이터 일관성을 유지하기 어려움
    - 순서 보장 및 중복을 방지하기 위한 로직이 추가되기도 함

> 과제에서는 이벤트 기반 시스템을 구현하기 어려우므로 어플리케이션 레벨에서 구현

executor 설정

- 작업이 cpu intensive -> maxPoolSize 를 core 와 같게
- 작업이 io intensive -> maxPoolSize 를 core 보다 크게 2배, 4배
- AsyncUncaughtExceptionHandler
  - 비동기 메서드를 실행할 때 발생하는 예외를 처리
- RejectedExecutionHandler
  - 스레드 풀이 작업 거부할 때 발생하는 예외 처리
  - 작업 큐 가득 차거나 스레드 풀 최대 용량 초과 시 발생