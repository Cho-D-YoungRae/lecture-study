# 의존관계 자동 주입

## 다양한 의존관계 주입 방법

1. 생성자 주입
2. setter 주입
3. 필드 주입
4. 일반 메서드 주입

### 생성자 주입

> 생성자를 통해서 의존관계 주입

- 생성자 호출시점에 딱 1번만 호출되는 것이 보장
- **불변**, **필수** 의존관계에 사용
- 생성자가 딱 1개만 있으면 `@Autowired` 생략 가능

### setter 주입

> setter라 불리는 필드의 값을 수정하는 수정자 메서드를 통해서 의존관계를 주입하는 방법

- **선택**, **변경** 가능성이 있는 의존관계에 사용
- 자바빈 프로퍼티 규약의 수정자 메서드 방식을 사용하는 방법
- 의존관계 주입 시점
  - 생성자 주입: 객체를 생성해야 함 -> 빈을 등록하면서 의존관계 주입이 같이 됨
  - setter 주입 등 생성자 주입을 제외한 주입: 스프링 빈 등록 단계 이후 의존 관계 주입 단계에서 일어남

### 필드 주입

> 필드에 바로 주입하는 방법

- 코드가 간결해서 많은 개발자들을 유혹하지만 외부에서 변경이 불가능해서 테스트하기 힘들다는 치명적인 단점이 있음
- DI 프레임워크가 없으면 아무것도 할 수 없음
  - 순수 자바로 테스트하는 경우가 많음
  - 순수 자바 테스트 코드에는 당연히 `@Autowired`가 동작하지 않음 -> `@SpringBootTest`처럼 스프링 컨테이너를 테스트에 통합한 경우에만 가능
- 사용하지 말자! 아래와 같은 경우만 사용
  - 애플리케이션의 실제 코드와 관계없는 테스트 코드
  - 스프링 설정을 목적으로 하는 `@Configuration` 같은 곳에서만 특별한 용도로 사용

### 일반 메서드 주입

> 일반 메서드를 통해서 주입

- 한번에 여러 필드를 주입 받을 수 있음
- 일반적으로 잘 사용하지 않음

### 옵션 처리

주입할 스프링 빈이 없어도 동장해야 할 때가 있는데, `@Autowired`만 사용하면 `required` 옵션의 기본값이 `true`로 되어 있어서 자동 주입 대상이 없으면 오류가 발생한다. 

자동 주입 대상을 옵션으로 처리하는 방법은 다음과 같다.

- `@Autowired(required=false)`: 자동 주입할 대상이 없으면 수정자 메서드 자체가 호출 안됨
- `org.springframeword.lang.@Nullable`: 자동 주입할 대상이 없으면 null 입력
- `Optional<T>`: 자동 주입할 대상이 없으면 `Optional.empty`가 입력

### 생성자 주입을 선택하라

> 과거에는 수정자 주입과 필드 주입을 많이 사용했지만, 최근에는 스프링을 포함한 DI 프레임워크 대부분이 다음과 같은 이유로 생성자 주입을 권장한다.

- `불변`
  - 대부분의 의존관계 주입은 한번 일어나면 애플리케이션 종료시점까지 의존관계를 변경할 일이 없다. 오히려 대부분의 의존관계는 애플리케이션 종료 전까지 변하면 안된다.(불변해야한다.)
  - 수정자 주입을 사용하면 setXxx 메서드를 public으로 열어두어야 한다.
  - 누군가 실수로 변경할 수도 있고, 변경하면 안되는 메서드를 열어두는 것은 좋은 설계 방법이 아니다.
  - 생성자 주입은 객체를 생성할 때 딱 1번만 호출되므로 이후에 호출되는 일이 없다. 따라서 불변하게 설계할 수 있다.
- `누락`
  - 프레임워크 없이 순수한 자바 코드를 단뒤 테스트 하는 경우
  - `@Autowired`가 프레임워크 안에서 동작할 때는 의존관계가 없으면 오류가 발생하지만, 프레임워크 없이 순수한 자바 코드로만 단위 테스트 할 때는 발생하지 않음(실행될 때 Null Point Exception 이 발생)
  - 생성자 주입을 사용하면 주입 데이터를 누락 했을 때 컴파일 오류 발생
- `final 키워드`
  - 생성자 주입을 사용하면 필드에 `final` 키워드를 사용할 수 있음
  - 생성자에서 혹시라도 값이 설정되지 않는 오류를 컴파일 시점에 막아줄 수 있음

### `@Autowired` 필드 명, `@Qualifier`, `@Primary`

#### 조회 빈이 2개 이상일 때 해결 방법

- `@Autowired` 필드 명 매칭
- `@Qualifier` -> `@Qualifier`끼리 매칭 -> 빈 이름 매칭
- `@Primary` 사용

#### `@Autowired` 매칭

1. 타입 매칭
2. 타입 매칭 결과가 2개 이상일 때 필드 명, 파라미터 명으로 빈 이름 매칭

#### `@Qualifier` 사용

> 추가 구분자를 붙여주는 방법이다. 주입 시 추가적인 방법을 제공하는 것이지 빈 이름을 변경하는 것은 아니다.

빈 등록시 `@Qualifier`를 붙여줌

```java
@Component
@Qualifier("mainDiscountPolicy")
public class RateDiscountPolicy implements DiscountPolicy {}
```

주입시에 `@Qualifier`를 붙여주고 등록한 이름을 적어줌

```java
@Autowired
public OrderServiceImpl(@Qualifier("mainDiscountPolicy") DiscountPolicy discountPolicy) {
  this.discountPolicy = discountPolicy;
}
```

- `@Qualifier`끼리 매칭
- 없으면 `@Qualifier`에 파라미터로 준 이름으로 빈 이름 매칭
  - `@Qualifier`를 찾는 용도로만 사용하는게 명확하고 좋음
- 없으면 `NoSuchBeanDefinitionException` 예외 발생

#### `@Primary` 사용 -> 자주 사용됨

> 우선순위를 정하는 방법이다. `@Autowired`시에 여러 빈이 매칭되면 `@Primary`가 우선권을 가진다.

#### `@Primary`, `@Qualifier` 활용

- 코드에서 자주 사용하는 메인 데이터베이스의 커넥션을 획득하는 스프링 빈
  - `@Primary`를 적용해서 조회하는 곳에서 `@Qualifier`없이 편리하게 조회
  - `@Qualifier`를 지정해주어도 상관없음
- 코드에서 특별한 기능으로 가끔 사용하는 서브 데이터베이스의 커넥션을 획득하는 스프링 빈
  - `@Qualifier`를 지정해서 명시적으로 획득
- `@Qualifier` 애너테이션을 만들어서 사용할 수 있음
  - `@Qualifier`의 문자열파라미터는 컴파일시 타입 체크가 안됨

### 조회한 빈이 모두 필요할 때 List, Map

- `Map<Striong, DiscountPolicy>`: map의 키에 스프링 빈의 이름을 넣어주고, 그 값으로 `DiscountPolicy` 타입으로 조회한 모든 스프링 빈을 담아줌
- `List<DiscountPolicy>`: `DiscountPolicy` 타입으로 조회한 모든 스프링 빈을 담아줌
- 만약 해당하는 타입의 스프링 빈이 없으면, 빈 컬렉션이나 Map을 주입

## 자동, 수동의 올바른 실무 운영 기준

### 편리한 자동 기능을 기본으로 사용

- 스프링이 나오도 시간이 갈 수록 자동을 선호하는 추세
  - `@Component` 뿐만 아니라 `@Controller`, `@Repository`, `@Service` 처럼 계층에 맞추어 일반적인 애플리케이션 로직을 자동으로 스캔할 수 있도록 지원
  - 최근 스프링 부트는 컴포넌트 스캔을 기본으로 사용
  - 스프링 부트의 다양한 스프링 빈들도 조건이 맞으면 자동으로 등록하도록 설계
- 관리할 빈이 많아서 설정 정보가 커지면 설정 정보를 관리하는 것 자체가 부담
- 자동 빈 등록을 사용해도 OCP, DIP 를 지킬 수 있음

### 수동 빈 등록은 언제 사용?

- `업무 로직 빈`: 업무로직은 숫자도 매우 많고, 한번 개발해야 하면 컨트롤러, 서비스, 리포지토리처럼 어느정도 유사한 패턴이 있다. 이런 경우 **자동 기능**을 적극 사용하는 것이 좋다. 보통 문제가 발생해도 어떤 곳에서 문제가 발생했는지 명확하게 파악하기 쉽다.
  - 웹을 지원하는 컨트롤러, 핵심 비즈니스 로직이 있는 서비스, 데이터 계층의 로직을 처리하는 리포지토리 등 모두 업무 로직
  - 보통 비즈니스 요구사항을 개발할 때 추가되거나 변경
- `기술 지원 빈`: 업무 로직과 비교해서 그 수가 매우 적고, 보통 애플리케이션 전반에 걸쳐서 광범위하게 영향을 미친다. 그리고 업무 로직은 문제가 발생했을 때 어디가 문제인지 명확하게 잘 들어나지만, 기술 지원 로직은 적용이 잘 되고 있는지 아닌지 조차 파악하기 어려운 경우가 많다. 그래서 이런 기술 지원 로직들은 가급적 수동 빈 등록을 사용해서 명확하게 들어내는 것이 좋다.
  - 기술적인 문제나 공통 관심사(AOP)를 처리할 때 주로 사용
  - 데이터베이스 연결이나 공통 로그 처리 처럼 업무 로직을 지원하기 위한 하부 기술이나 공통 기술 들