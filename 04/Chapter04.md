# 공통 인터페이스 기능

- `@EnableJpaRepositories(basePackages = "...")` 선언
  - 스프링 부트 사용시 생략 가능
  - 스프링 부트 사용시 `@SpringBootApplication` 위치를 지정 (해당 패키지와 하위 패키지 인식)
- `org.springframework.data.repository.JpaRepository` 를 구현한 클래스는 스캔 대상
- `@Repository` 애노테이션 생략 가능
