# 빈 스코프

- `싱글톤`: 기본 스코프, 스프링 컨테이너의 시작과 종료까지 유지되는 가장 넓은 범위의 스코프
- `프로토타입`: 스프링 컨테이너는 프로토타입 빈의 생성과 의존관계 주입까지만 관여하고 더는 관리하지 않는 매우 짧은 범위의 스코프
- `웹 관련 스코프`
  - `request`: 웹 요청이 들어오고 나갈때까지 유지되는 스코프
  - `session`: 웹 세션이 생성되고 종료될 때 까지 유지되는 스코프
  - `application`: 웹의 서블릿 컨텍스트와 같은 범위로 유지되는 스코프

빈 스코프 지정 방법

```java
@Scope("prototype")
@Component
public class HelloBean {}
```

```java
@Scope("prototype")
@Bean
public PrototypeBean HelloBean() {
    return new HelloBean();
}
```

## 프로토타입 스코프

- 항상 새로운 인스턴스를 생성해서 반환
- 스프링 컨테이너는 프로토타입 빈을 생성하고, 의존관계 주입, 초기화 까지만 처리
  - 생성된 프로토타입 빈을 관리하지 않음
  - 프로토 타입 빈을 관리할 책임은 프로토타입 빈을 받은 클라이언트에 있음
  - `@PreDestroy` 같은 종료 메서드가 호출되지 않음

## 프로토타입 스코프 - 싱글톤 빈과 함께 사용시 문제점

스프링은 일반적으로 싱글톤 빈을 사용하므로, 싱글톤 빈이 프로토타입 빈을 사용하게 됨다. 그런데 싱글톤 빈은 생성 시점에만 의존관계 주입을 받기 때문에, 프로토타입 빈이 새로 생성되기는 하지만, 싱글톤 빈과 함께 계속 유지되는 것이 문제다.

아마 원하는 것이 이런 것은 아닐 것이다. 프로토타입 빈을 주입 시점에만 새로 생성하는 것이 아니라, 사용할 때마다 새로 생성해서 사용하는 것을 원할 것이다.

여러 빈에서 같은 프로토타입 빈을 주입 받으면, 주입 받는 시점에 각각 새로운 프로토타입 빈이 생성된다. 물론 사용할 때마다 새로 생성되는 것은 아니다.

Client 에서 @Autowired ApplicationContext 주입받고 prototypeBean 필요할 때마다 getBean() 해서 사용하면 너무 지저분해진다. 좋은 방법이 아니다. ApplicationContext 를 주입받으면 스프링 컨테이너에 종속적인 코드가 되고, 단위 테스트도 어려워 지고, 빈 조회 이외에 너무 많은 기능을 가지고 있다.

## 프로토타입 스코프 - 싱글톤 빈과 함께 사용시 Provider로 문제 해결

### 스프링 컨테이너에 요청

- `applicationContext.getBean()` 을 통해서 빈을 조회하면 항상 새로운 프로토타입 빈이 생성
- 스프링의 애플리케이션 컨텍스트 전체를 주입받게 되면, 스프링 컨테이너에 종속적인 코드가 되고, 단위 테스트도 어려워짐
- 지금 필요한 기능은 지정한 프로토타입 빈을 컨테이너에서 대신 찾아주는 딱 DL(Dependency Lookup, 의존관계 조회) 정도의 기능만 있으면 됨

### ObjectFactory, ObjectProvider

```java
@Autowired
private ObjectProbider<PrototypeBean> prototypeBeanProvider;

public void logic() {
    PrototypeBean prototypeBean = prototypeBeanProvider.getObject();
    ...
}
```

- `prototypeBeanProvider.getObject()`을 통해서 항상 새로운 프로토타입 빈이 생성
- `ObjectProvider.getObject()` 를 호출하면 내부에서는 스프링 컨테이너를 통해 해당 빈을 찾아서 반환 (**DL**)
- 스프링이 제공하는 기능을 사용하지만, 기능이 단순하므로 단위테스트를 만들거나 mock 코드를 만들기는 훨씬 쉬워짐
- 지금 딱 필요한 DL 정도의 기능만 제공
- `ObjectFactory`: 기능이 단순. 별도의 라이브러리 필요 없음. 스프링에 의존.
- `ObjectProvider`: `ObjectFactory` 상속. 옵션, 스트림 처리 등의 편의 기능이 많음. 별도의 라이브러리 필요 없음. 스프링에 의존.
- 스프링이 빈으로 만들어서 주입해줌

### JSR-330 Provider

```java
@Autowired
private Provider<PrototypeBean> prototypeBeanProvider;

public void logic() {
    PrototypeBean prototypeBean = prototypeBeanProvider.get();
    ...
}
```

- `javax.inject.Provider`. `javax.inject:javax.inject:1` 라이브러리를 추가해야되는 단점
- `prototypeBeanProvider.get()`을 통해서 항상 새로운 프로토타입 빈이 생성
- `Provider.get()` 을 호출하면 내부에서는 스프링 컨테이너를 통해 해당 빈을 찾아서 반환(**DL**)
- 자바 표준이고, 기능이 단순하므로 단위테스트를 만들거나 mock 코드를 만들기는 훨씬 쉬어짐
- 지금 딱 필요한 DL 정도의 기능만 제공
- `get()` 메서드 하나로 기능이 매우 단순
- 자바 표준이므로 스프링이 아닌 다른 컨테이너에서도 사용 가능

### 정리

- 포로토 타입 빈을 언제 사용? -> 매번 사용할 때마다 의존관계 주입이 완료된 새로운 객체가 필요할 때
- 실무에서 웹 애플리케이션을 개발할 때는 싱글톤 빈으로 대부분의 문제를 해결할 수 있기 때문에 프로토타입 빈을 직접적으로 사용하는 일은 드뭄
- `ObjectProvider`, `JSR330 Provider` 등은 프로토타입 뿐만 아니라 DL이 필요한 경우는 언제든지 사용 가능 (프로토타입 전용 X)
- 스프링이 제공하는 메서드에 `@Lookup` 애노테이션을 사용하는 방법도 있지만, 이전 방법들로 충분하고 고려해야할 내용도 많음
- 자바 표준과 스프링이 제공하는 기능이 겹칠때는 스프링이 더 다양하고 편리한 기능을 제공해주기 때문에 특별히 다른 컨테이너를 사용할 일이 없다면 스프링이 제공하는 기능을 사용하면 됨

## 웹스코프

### 웹스코프 특징

- `request`: HTTP 요청 하나가 들어오고 나갈 때까지 유지되는 스코프, 각각의 HTTP 요청마다 별도의 빈 인스턴스가 생성되고 관리
- `session`: HTTP Session 과 동일한 생명주기를 가지는 스코프
- `application`: 서블릿 컨텍스트(ServletContext)와 동일한 생명주기를 가지는 스코프
- `websocket`: 웹 소켓과 동일한 생명주기를 가지는 스코프

### request 스코프 예제 개발

request 스코프로 스프링 빈을 만들어서 스프링 애플리케이션을 실행 시키면 오류가 발생한다. 메시지 마지막에 싱글턴이라는 단어가 나오고...
스프링 애플리케이션을 실행하는 시점에 싱글톤 빈은 생성해서 주입 가능하지만, request 스코프 빈은 아직 생성되지 않는다. 이 빈은 실제 고객의 요청이 와야 생성할 수 있다.

### 스코프와 Provider

- `Provider` 를 통해서 위 문제 해결 가능
- `ObjectProvider` 를 통해서 request scope 빈의 **생성을 지연**할 수 있음
- `ObjectProvider.getObject()`를 호출하는 시점에는 HTTP 요청이 진행중이므로 request scope 빈의 생성이 정상 처리됨
- `ObjectProvider.getObject()` 를 Controller, Service 에서 각각 호출해도 같은 HTTP 요청이면 같은 스프링 빈이 반환

### 스코프와 프록시

```java
@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MyLogger { }
```

- `proxyMode = ScopedProxyMode.TARGET_CLASS`를 추가
  - 적용 대상이 클래스면 `TARGET_CLASS`
  - 적용 대상이 인터페이스면 `INTERFACES`
- 가짜 프록시 클래스를 만들어두고 HTTP request와 상관없이 가짜 프록시 클래스를 다른 빈에 미리 주입해 둘 수 있음
- 가짜 프록시 객체는 요청이 오면 그때 내부에서 진짜 빈을 요청하는 위임 로직이 있음
