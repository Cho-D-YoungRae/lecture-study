# 싱글톤 컨테이너

## 싱글톤 패턴 문제점

- 싱글톤 패턴을 구현하는 코드 자체가 많이 들어감
- 의존관계상 클라이언트가 구체 클래스에 의존 -> DIP 위반
  - ex) `MemberServiceImpl.getInstance()`
- 클라이언트가 구체 클래스에 의존해서 OCP 원칙을 위반할 가능성 높아짐
- 테스트 어려움
  - 설정이 다 된 객체가 박히므로 유연하게 테스트 어려움
- 내부 속성을 변경하거나 초기화 어려움
- privete 생성자로 자식 클래스 만들기 어려움
- 결론적으로 유연성 떨어짐
- 안티패턴으로 불리기도 함

## 싱글톤 컨테이너

> 싱글톤 패턴의 문제점을 해결하면서, 객체 인스턴스를 싱글톤으로 관리

- 스프링 컨테이너는 싱글턴 패턴을 적용하지 않아도, 객체 인스턴스를 싱글톤으로 관리
- 스프링 컨테이너는 싱글톤 컨테이너 역할을 함
  - 싱글톤 레지스트리: 싱글톤 객체를 생성하고 관리하는 기능
- 스프링 컨테이너의 이런 기능 덕분에 싱글턴 패턴의 모든 단점을 해결하면서 객체를 싱글톤으로 유지 가능
  - 싱글톤 패턴을 위한 지저분한 코드가 들어가지 않아도 됨
  - DIP, OCP, 테스트, private 생성자로부터 자유롭게 싱글톤 사용 가능

## 싱글톤 방식의 주의점

- 싱글톤 패턴이든, 스프링 같은 싱글톤 컨테이너를 사용하든, 객체 인스턴스를 하나만 생성해서 공유하는 싱글톤 방식은 여러 클라이언트가 하나의 같은 객체 인스턴스를 공유하기 때문에 싱글톤 객체는 상태틀 유지(stateful)하게 설계하면 안됨
- 무상태(stateless)로 설계
  - 특정 클라이언트에 의존적인 필드가 있으면 안됨
  - 특정 클라이언트가 값을 변경할 수 있는 필드가 있으면 안됨
  - 가급적 읽기만 가능하도록
  - 필드 대신 자바에서 공유되지 않는, 지역 변수, 파라미터, ThreadLocal 등을 사용해야 함
- 스프링 빈의 필드에 공유 값을 설정하면 정말 큰 장애가 발생 가능

## `@Configuration`과 바이트코드 조작의 마법

스프링은 아래와 같이 싱글톤을 보장 -> `memberRepository()` 여러번 호출해도 1개만 생성되도록

AppConfig 실제 코드

```java
@Configuration
public class AppConfig {

    @Bean
    public MemberRepository memberRepository() {
        return new MemberRepository();
    }
}
```

AppConfi@CGLIB 예상 코드

```java
@Configuration
public class AppConfig {

    @Bean
    public MemberRepository memberRepository() {
        if (memberRepository 가 이미 스프링 컨테이너에 등록되어 있으면?) {
            return 스프링 컨테이너에서 찾아서 반환;
        }
        else {
            return new MemberRepository();
        }
    }
}
```

- `@Bean`만 사용해도 스프링 빈으로 등록되지만, 싱글톤을 보장하지 않는다.
  - Config 객체를 ApplicationContext 에 직접 등록 해야됨 (컴포넌트 스캔이 안되므로)
  - `memberRepository()` 여러번 호출하면 계속 생성됨
  - memberRepository 가 스프링 빈으로 등록은 됨
  - 크게 고민할 것 없이 스프링 설정 정보는 항상 `@Configuration` 사용하면 됨
