package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

// @Transactional
// : DB 에 데이터를 insert 한 다음에 commit 을 해줘야 반영이 되는 것인데,
//   이를 사용하면 DB 에 쿼리를 주고 검증 후 롤백 한다.
// @SpringBootTest
// : Spring 을 띄워서 Spring 위에서 테스트 진행
@SpringBootTest
@Transactional
class MemberServiceIntegrationTest {

    // 원래는 Constructor 에서 설정해주었으나
    // 테스트 코드는 가장 끝단에 있는 것이므로 제잂 편한 방법을 이용하기도 한다(아래와 같이)
    // -> 테스트를 어디서 갖다 쓸 것이 아니기 때문에
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    // @Transactional 이 있으므로 아래 코드가 필요 없어 진다.
//    @AfterEach
//    public void afterEach() {
//        memberRepository.clearStore();
//    }

    @Test
    void 회원가입() {
        // Given
        Member member = new Member();
        member.setName("spring");

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
        memberService.join(member1);
        IllegalStateException e = assertThrows(
                IllegalStateException.class, () -> memberService.join(member2));

        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
        // Then
    }
}