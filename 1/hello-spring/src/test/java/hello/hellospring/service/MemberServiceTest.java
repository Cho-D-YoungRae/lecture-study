package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;


// 테스트 코드는 한글을 포함하기도 한다.
// 테스트 코드는 Given, When, Then 으로 나뉘는 경우가 많다.
// -> 이러한 상황으로 처음에는 연습하자. 이후 점점 변형, 응용
class MemberServiceTest {

    // 아래와 같이 하면
    // (아래 상황에서는 MemberService 클래스 안에서도 MemoryMemberRepository 객체를 갖고있다.)
    // 다른 MemoryMemberRepository 객체를 이용하고 있고,
    // 현재는 MemoryMemberRepository 안에서 static 으로 데이터를 관리하고 있어서 문제되지 않지만
    // 다른 상황에서는 이렇게 다른 객체를 이용하는 것은 좋지 않다.
//    MemberService memberService = new MemberService();
//    MemoryMemberRepository memberRepository = new MemoryMemberRepository();
    MemberService memberService;
    MemoryMemberRepository memberRepository;

    @BeforeEach
    public void beforeEach() {
        // MemberService 입장에서 MemoryMemberRepository 를 직접 생성하지 않고 외부에서 넣어준다.
        // Dependency Injection -> DI
        memberRepository = new MemoryMemberRepository();
        memberService = new MemberService(memberRepository);
    }

    @AfterEach
    public void afterEach() {
        memberRepository.clearStore();
    }

    @Test
    void 회원가입() {
        // Given
        Member member = new Member();
        member.setName("hello");

        // When
        Long saveId = memberService.join(member);

        // Then
        Member findMember = memberService.findOne(saveId).get();
        assertThat(member.getName()).isEqualTo(findMember.getName());
    }

    @Test
    public void 중복_회원_예외() {
        // Given
        Member member1 = new Member();
        member1.setName("spring");

        Member member2 = new Member();
        member2.setName("spring");

        // When
        // 아래와 같이 해도되지만 이를 위해서 try-catch 를 이용하는 것이 애매하다.
//        memberService.join(member1);
//        try {
//            memberService.join(member2);
//            // 예외가 발생해야 하므로 아래 코드가 실행되면 테스트가 실패한 것이다.
//            fail();
//        } catch (IllegalStateException e) {
//            // 예외가 정상적으로 발생되었다.
//            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
//        }
        memberService.join(member1);
        IllegalStateException e = assertThrows(
                IllegalStateException.class, () -> memberService.join(member2));

        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
        // Then

    }

    @Test
    void findMembers() {
    }

    @Test
    void findOne() {
    }
}