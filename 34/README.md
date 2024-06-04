# [Java/Spring 테스트를 추가하고 싶은 개발자들의 오답노트](https://www.inflearn.com/course/자바-스프링-테스트-개발자-오답노트/dashboard)

> [github](https://github.com/kok202/test-code-with-architecture)

- [x] 2024/05/11 ~ 2024/05/15
- [x] 2024/06/04 - pdf 복습

## 정리

- VO 의 특징 중 하나: 'VO 안의 변수들은 값이 항상 유효하다'

### 테스트 추가하기: h2 을 이용한 repository 테스트

```java
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByIdAndStatus_로_유저_데이터를_찾아올_수_있다() {
        // given
        final UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("kok202@naver.com");
        userEntity.setNickname("kok202");
        userEntity.setAddress("Seoul");
        userEntity.setCertificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
        userEntity.setStatus(UserStatus.ACTIVE);

        // when
        userRepository.save(userEntity);
        final Optional<UserEntity> result = userRepository.findByIdAndStatus(1L, UserStatus.ACTIVE);

        // then
        assertThat(result).isPresent();
    }

    @Test
    void findByIdAndStatus_는_데이터가_없으면_Optional_empty_를_내려준다() {
        // given
        final UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("kok202@naver.com");
        userEntity.setNickname("kok202");
        userEntity.setAddress("Seoul");
        userEntity.setCertificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
        userEntity.setStatus(UserStatus.ACTIVE);

        // when
        userRepository.save(userEntity);
        final Optional<UserEntity> result = userRepository.findByIdAndStatus(1L, UserStatus.PENDING);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void findByEmailAndStatus_로_유저_데이터를_찾아올_수_있다() {
        // given
        final UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("kok202@naver.com");
        userEntity.setNickname("kok202");
        userEntity.setAddress("Seoul");
        userEntity.setCertificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
        userEntity.setStatus(UserStatus.ACTIVE);

        // when
        userRepository.save(userEntity);
        final Optional<UserEntity> result = userRepository.findByEmailAndStatus("kok202@naver.com", UserStatus.ACTIVE);

        // then
        assertThat(result).isPresent();
    }

    @Test
    void findByEmailAndStatus_는_데이터가_없으면_Optional_empty_를_내려준다() {
        // given
        final UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("kok202@naver.com");
        userEntity.setNickname("kok202");
        userEntity.setAddress("Seoul");
        userEntity.setCertificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
        userEntity.setStatus(UserStatus.ACTIVE);

        // when
        userRepository.save(userEntity);
        final Optional<UserEntity> result = userRepository.findByEmailAndStatus("kok202@naver.com", UserStatus.PENDING);

        // then
        assertThat(result).isEmpty();
    }
}
```

각각의 테스트에 save 가 있으면 테스트를 각각 실행시킬 때는 성공하지만, 전체를 실행시킬 때는 몇몇 테스트가 실패하는 문제

- 대표적인 비결정적 문제
- 테스트 메서드가 병렬로 처리되는데 동시성 제어가 안되는 것으로 보임

### 테스트 추가하기: h2 를 이용한 service 테스트

수정 테스트에서는 수정하려는 엔티티의 값이 정상적으로 수정되었는지와 수정하지 않은 다른 값이 수정되지 않았는지를 확인할 수 있음 -> 강의에서는 수정된 값만 체크

### 패키지 구조 개선

CreateDto, UpdateDto 등 컨트롤러 쪽 뷰로 생각하고 컨트롤러 패키지 하위로 이동시키려 했으나, 서비스에서 참조되어야 하는 것이므로 도메인 패키지에 두는 것이 맞다고 봄

### 외부 연동을 다루는 방법

service 패키지의 port 패키지: 외부 연동을 담당. 서비스에서 사용하는 외부 연동 인터페이스

### 도메인과 영속성 객체 구분하기

UserEntity.toModel() 메서드로 도메인 객체로 변환하는 것 처럼 User.toEntity() 를 두는 것을 생각할 수 있지만 도메인은 인프라 레이어의 정보를 모르는 것이 좋음

### 서비스를 소형 테스트로 만들기

테스트를 위한 Fake 레포지토리에서 데이터를 저장하기 위해 `Collections.synchronizedList` 사용하고 있는데, 소형 테스트는 단일 스레드에서 돌아가서 동기화를 걱정할 필요가 없으므로 그냥 `ArrayList` 사용해도 됨

`UserRepository` 가 `save`만 되어있는데 id 가 null 이거나 0 이면 insert 하고 아니면 update 하는 것은 jpa 의존적 -> `create`, `update` 구분하는 것이 좋아 보임

PostService 를 테스트할 때 PostService 가 의존하던 UserService 를 UserRepository 로 변경함 -> 귀찮아서? 맞다!

- 테스트를 작성하다 보니까 유저 서비스를 또 생성해서 주입해주는 것이 귀찮아서 레포지토리에 의존하게 바꿔준 것
- mailSender 나 uuid 같은 것은 postService 에서 사용하지도 않는데 이것을 생성해서 UserService 에 넣어주고 이 UserService 를 PostService 에 넣어주어야 하기 때문 -> 번거로움
- 테스트가 신호를 보내는 것 -> 테스트 짜는 것이 귀찮게 느껴진다면 의존성을 줄이라고!

### 컨트롤러를 소형 테스트로 만들기

`CertificationService` 는 외부(컨트롤러)에서 사용되지 않으므로 추상화 하지 않음

### 마지막 리팩토링

도메인 객체가 사용자에게 노출되지 않도록 -> 컨트롤러의 RequestBody 로 사용되던 UserUpdate 를 UserUpdateRequest 로 변경할 수 있음. UserUpdateRequest.to() 메서드도 만들 수 있음

컨트롤러의 반환타입이 다르다는 것은 책임이 제대로 분할 안 된 것 같은 느낌이 듬 -> MyInfoController 분리할 수 있음
