# 프로젝트 환경설정

- JPA 스펙에 의해 Entity 는 default 생성자가 필요
  - 프록시 기술을 사용하므로
  - 밖에서 사용하지 않도록 하려면 protected 로 선언
- JPA 는 모든 데이터 변경을 트랜잭션 안에서
  - 스프링 테스트가 `@Transactional` 이 있으면 끝나고 데이터 롤백
  - 롤백을 안 시키려면 `@Rollback(false)`
- 쿼리 파라미터 로그 남기기
  - 로그에 다음을 추가 `org.hibernate.type`
  - 외부 라이브러리 사용 p6spy
