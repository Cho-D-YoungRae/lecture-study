# [Spring Boot JWT Tutorial](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8-jwt/dashboard)

> `inflearn` - `정은구`

- [x] 2022/03/24 ~ 2022/03/24

## github

- <https://github.com/SilverNine/spring-boot-jwt-tutorial>

## Note

### JWT 장점

- 중앙의 인증서버, 데이터 스토어에 대한 의존성 없음
  - 시스템 수평 확장 유리
- Base64 URL Safe Encoding > URL, Cookie, Header 모두 사용 가능

### JWT 단점

- Payload의 정보가 많아지면 네트워크 사용량 증가, 데이터 설계 고려 필요
- 토큰이 클라이언트에 저장, 서버에서 클라이언트의 토큰을 조작할 수 없음
