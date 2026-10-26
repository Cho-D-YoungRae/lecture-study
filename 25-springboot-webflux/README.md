# [Springboot WebFlux 강좌](https://www.youtube.com/watch?v=Sz1Ve_1KZII&list=PL93mKxaRDidFH5gRwkDX5pQxtp0iv3guf&index=1)

> `YouTube` - `메타코딩`

## Reactive Programming 배경

- 요청이 있을 때 멍때리지 않고 응답
- 물어보지 않아도 알려줄 수 있도록

## WebFlux 탄생

`Client` - `Server` - `DB`

- `Client` 는 비동기 처리 하면 됨
- `Server` 가 멍을 때리지 않는게 중요
- `Client` 가 요청하고 `Server`가 멍때리는 동안 다른 `Client`의 요청을 받지 못 함
- 이를 해결하기 위해
  - 스레드 생성 (Time 슬라이싱)
    - 컨텍스트 스위칭의 단점
  - 비동기 처리
    - 즉각적인 응답 -> 처리 예상 시간을 알려줌
    - 그 동안 다른 요청을 받을 수 있음
    - 처리 예상 시간이 지난 후 응답 해주어야 함
    - 처리하지 못 한 이벤트에 대한 기억 공간이 필요. 이 기억공간을 `이벤트 루프`라고 함
    - 요청을 처리하지 않고 쉬고 있는 시간에 `Server`는 `이벤트 루프`를 확인하고 처리 예상 시간이 지나 처리된 요청을 응답
    - 멍을 안 때리려면 -> 이벤트 루프
    - 물어보지 않아도 알려주려면 -> 응답 유지 `Stream` (요청은 끊어도 됨) -> SSE (Server Send Event) 프로토콜 이용
- WebFlux
  - 구독 (Subscribe): 응답이 유지
  - 출판 (Publish): 유지되는 연결로 응답

## 노가다 코드 짜보기

- sse 프로토콜은 WebFlux 가 아니라 Spring Servlet MVC 에서도 동작함
- 차이는 `Spring Servlet MVC`는 쓰레드 기반 `WebFlux`는 단일 쓰레드 이벤트 루프 기반
