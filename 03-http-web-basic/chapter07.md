# Chapter 7. HTTP 헤더1 - 일반 헤더

## 전송 방식

- 단순 전송
- 압축 전송
  - Content-Encoding 헤더 필요
- 분할 전송
  - Transfer-Encoding: chunked
  - 먼저 도착한대로 처리할 수 있음
  - Content-Length 넣으면 X
    - 예상할 수 X
    - chunked 마다 길이가 포함되어 있음
- 범위 전송
  - Content-Range: bytes 1001-2000 / 2000
  - 예를 들어 이미지가 전송되다가 끊겼는데 다시 전송받을 때 처음부터 전송되면 아까움

## 일반 정보

- `From`: 유저 에이전트의 이메일 정보
  - 일반적으로 잘 사용되지 않음
  - 검색 엔진 같은 곳에서 주로 사용
  - 요청에서 사용
- `Referer`: 이전 웹 페이지 주소
  - A -> B 로 이동하는 경우 B를 요청할 때 Referer: A 를 포함해서 요청
  - 유입 경로 분석 가능
  - 요청에서 사용
- `User-Agent`: 유저 에이전트 애플리케이션 정보
  - 클라이언트의 애플리케이션 정보(웹 브라우저 정보 등등)
  - 통계 정보
  - 어떤 종류의 브라우저에서 장애가 발생하는지 파악 가능
  - 요청에서 사용
- `Server`: 요청을 처리하는 오리진 서버의 소프트웨어 정보
  - 응답에서 사용
- `Date`: 메시지가 생성된 날짜
  - 응답에서 사용

## 특별한 정보

- `Host`: 요청한 호스트 정보(도메인)
  - 요청에서 사용
  - **필수**
  - 하나의 서버가 여러 도메인을 처리해야 할 때
  - 하나의 IP 주소에 여러 도메인이 적용되어 있을 때
- `Location`: 페이지 리다이렉션
  - 웹 브라우저는 3xx 응답의 결과에 Location 헤더가 있으면 Location 위치로 자동 이동 (리다이렉트)
  - 201 (Created): Location 값은 요청에 의해 생성된 리소스 URI
  - 3xx (Redirection): Location 값은 요청을 자동으로 리다이렉션하기 위한 대상 리소스를 가리킴
- `Allow`: 허용 가능한 HTTP 메소드
  - 405 (Method Not Allowed) 에서 응답에 포함해야함
  - Allow: GET, HEAD, PUT
- `Retry-After`: 유저 에이전트가 다음 요청을 하기까지 기다려야 하는 시간
  - 503 (Service Unavailable): 서비스가 언제까지 불능인지 알려줄 수 있음
  - Retry-After: Fri, 31 Dec 1999 23:59:59 GMT (날짜 표기)
  - Retry-After: 120 (초단위 표기)

## 인증

- `Authorization`: 클라이언트 인증 정보를 서버에 전달
- `WWW-Authorization`: 리소스 접근시 필요한 인증 방법 정의

## 쿠키

- HTTP 는 Stateless 프로토콜이므로 쿠키를 사용하지 않고 이전 상태를 유지하려면 모든 요청에 사용자 정보가 포함되도록 개발해야함
- `Set-Cookie`: 서버에서 클라이언트로 쿠키 전달 (응답)
- `Cookie`: 클라이언트가 서버에서 받은 쿠키를 저장하고 HTTP 요청시 서버로 전달
- 사용처
  - 사용자 로그인 세선 관리
  - 광고 정보 트래킹
- 쿠키 정보는 항상 서버에 전송됨
  - 네트워크 트래픽 추가 유발
  - 최소한의 정보만 사용(세선 id, 인증 토큰)
  - 서버에 전송하지 않고, 웹 브라우저 내부에 데이터를 저장하고 싶으면 웹스토리지 (LocalStorage, SessionStorage) 참고
- 주의!
  - 보안에 민감한 데이터는 저장하면 안됨 (주민번호, 신용카드 번호 등등...)

### 쿠키 - 생명주기

- Set-Cookie: **expires**=Sat, 26-Dec-2020 04:39:21 GMT
  - 만료일이 되면 쿠키 삭제
- Set-Cookie: **max-age**=3600 (3600초)
  - 0이나 음수를 지정하면 쿠키 삭제
- 세션 쿠기: 만료 날짜를 생략하면 브라우저 종료시 까지만 유지
- 영속 쿠키: 만료 날짜를 입력하면 해당 날짜까지 유지

### 쿠키 - 도메인

- 예) domain=example.org
- 명시: 명시한 문서 기준 도메인 + 서브 도메인 포함
  - domain=example.org를 지정해서 쿠키 생성
  - example.org 는 물론이고
  - dev.example.org 도 쿠키 접근
- 생략: 현재 문서 기준 도메인만 적용
  - example.org에서 쿠키를 생성하고 domain 지정을 생략
  - example.org 에서만 쿠키 접근
  - dev.example.org는 쿠키 미접근

### 쿠키 - 경로

- 예) path=/home
- 이 경로를 포함한 하위 경로 페이지만 쿠키 접근
- 일반적으로 path=/ 루트로 지정
- 예) path=/home 지정
  - /home -> 가능
  - /home/level1 -> 가능
  - /home/level1/level2 -> 가능
  - /hello -> 불가능

### 쿠키 - 보안

- Secure
  - 쿠키는 http, https를 구분하지 않고 전송
  - Secure를 적용하면 https인 경우에만 전송
- HttpOnly
  - XSS 공격 방지
  - 자바스크립트에서 접근 불가(document.cookie)
  - HTTP 전송에만 사용
- SameSite
  - XSRF 공격 방지
  - 요청 도메인과 쿠키에 설정된 도메인이 같은 경우만 쿠키 전송
