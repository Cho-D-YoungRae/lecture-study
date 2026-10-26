# 쿼리 메소드 기능

- 메소드 이름으로 쿼리 생성
- ~~메소드 이름으로 JPA NamedQuery 호출~~
  - 사용 X -> 아래 것 사용
- `@Query` 어노테이션을 사용해서 리파지토리 인터페이스에 쿼리 직접 정의

## 메소드 이름으로 쿼리 생성

- 쿼리 메소드 필터 조건: [스프링 데이터 JPA 공식 문서](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation)
- 조회: find...By, read...By, query...By, get...By
  - [공식문서 참고](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-creation)
- COUNT: count...By -> `long`
- EXISTS: exists...By -> `boolean`
- DELETE: delete...By, remove...By -> `long`
- DISTINCT: find...DistinctBy
- LIMIT: findFirst3, findFirst, findTop, findTop3
  - [공식문서 참고](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.limit-query-result)

> 이 기능은 엔티티의 필드명이 변경되면 인터페이스에 정의한 메서드 이름도 꼭 함께 변경해야 한다. 그렇지 않으면 애플리케이션을 시작하는 시점에 오류가 발생한다.  
> 이렇게 애플리케이션 로딩 시점에 오류를 인지할 수 있는 것이 스프링 데이터 JPA 의 매우 큰 장점이다.

## ~~JPA NamedQuery~~ -> 실무에서 사용할 일 X

### `@NamedQuery` 어노테이션으로 Named 쿼리 정의

```java
@Entity
@NamedQuery(
    name="Member.findByUsername",
    query="select m from Member m where m.username = :username"
)
public class Member {...}
```

### JPA 를 직접 사용해서 Named 쿼리 호출

```java
public class MemberRepository {
    public List<Member> findByUsername(String username) {
        ...
        List<Member> resultList =
            em.createNamedQuery("Member.findByUsername", Member.class)
                .setParameter("username", username)
                .getResultList();
    } 
}
```

### 스프링 데이터 JPA 로 NamedQuery 사용

```java
public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);
}
```

`@Query` 를 생략하고 메서드 이름만으로 Named 쿼리를 호출할 수 있다.

### 정리

- 스프링 데이터 JPA 는 선언한 "도메인 클래스 + . + 메서드이름" 으로 Named 쿼리를 찾아서 실행
- 만약 실행할 Named 쿼리가 없으면 메서드 이름으로 쿼리 생성 전략 사용
- 필요하면 정략을 변경할 수 있지만 권장 X
- NamedQuery 는 애플리케이션 로딩시점에 검증
  - 문법 오류가 있으면 알려줌

> 스프링 데이터 JPA 를 사용하면 실무에서 NamedQuery 를 직접 등록해서 사용하는 일은 드물다. 대신 `@Query`를 사용해서 리파지토리 메서드에 쿼리를 직접 정의
