# Chapter 3. HTTP 메시지

## 역사

- HTTP/1.1: 가장 많이 사용. 대부분의 기능 포함
- HTTP/2: 성능 개선
- HTTP/3: 성능 개선. TCP 대신 UDP 사용

## 특징

- 클라이언트 서버 구조
  - 요청 - 응답
- 무상태 프로토콜(Stateless), 비연결성
- HTTP 메시지
- 단순함, 확장 가능

## 무상태 프로토콜 (Stateless)

> 서버가 클라이언트 상태를 보존 X

- 서버가 확장성 높음 (스케일 아웃)
- 클라이언트가 여러 서버 중 아무 서버나 호출해도 됨
  - 중간에 특정 서버가 장애 등 상황
- 클라이언트가 추가 데이터 전송
- 모든 것을 무상태로 설계할 수 있는 경우도 있고 없는 경우도 있지만 최대한 무상태로 설계하기 위해 노력
  - 로그인 등의 상황

## 비연결성

- HTTP는 기본이 연결을 유지하지 않는 모델
- 일반적으로 초 단위 이하의 빠른 속도로 응답
- 1시간 동안 수천명이 서비스를 사용해도 실제 서버에서 동시에 처리하는 요청은 수십개 이하로 매우 작음
- 서버에서 자원을 효율적으로 사용할 수 있음

### 한계와 극복

- TCP/IP 연결을 새로 맺어야 함 - 3 way handshake 시간 추가
- 웹 브라우저로 사이트를 요청하면 HTML 뿐만 아니라 자바스크립트, css, 추가 이미지 등 수 많은 자원이 함께 다운로드 -> 자원 하나마다 연결하고 끊으면서 비효율성 증가
- HTTP 지속 연결(Persistent Connections)로 문제해결
- HTTP/2, HTTP/3 에서는 더 많은 최적화

## HTTP 메시지

```
start-line
* ( header-field CRLF )
CRLF
[ message-body ]
```

### 시작 라인

- Request: method SP request-target SP HTTP-version CRLF
- Response: HTTP-version SP status-code SP reason-phrase

### HTTP 헤더

- header-field = field-name ":" OWS field-value OWS (OWS: 띄어쓰기 허용)
- field-name 은 대소문자 구분 없음
- field-value 는 구분함
- HTTP 전송에 필요한 모든 부가 정보
- 예) 메시지 바디의 내용, 메시지 바디의 크기, 압축, 인증, 요청 클라이언트(브라우저) 정보, 서버 애플리케이션 정보, 캐시 관리 정보...
- 표준 헤더가 많이 있음
- 필요시 임의의 헤더 추가 가능
  - 추가되면 이 헤더가 약속된 서버-클라이언트만 이해할 수 있음

### HTTP 메시지 바디

- 실제 전송할 데이터
- HTML 문서, 이미지, 영상, JSON 등등 byte 로 표현할 수 있는 모든 데이터 전송 가능
