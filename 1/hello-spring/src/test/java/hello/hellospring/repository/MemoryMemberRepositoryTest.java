package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

// 다른 곳에서 가져다 사용할 것이 아니므로 public 으로 선언하지 않아도 된다.
class MemoryMemberRepositoryTest {

    MemoryMemberRepository repository = new MemoryMemberRepository();

    // 테스트의 실행 순서는 보장되지 않는다 -> 실행 순서에 상관없이 동작이 보장되어야 한다.
    // 아래 코드가 테스트 실행이 끝날 때 마다 실행이 된다.
    // -> 실행 순서에 상관 없도록 저장소 혹은 공용 데이터를 초기화
    @AfterEach
    public void afterEach() {
        repository.clearStore();
    }

    // @Test: 테스트를 위해 -> 아래 메소드가 실행된다.
    @Test
    public void save() {
        Member member = new Member();
        member.setName("spring");

        repository.save(member);

        // 실무에서 Optional.get() 으로 바로 가져오는 것이 좋지는 않다고 함.
        // 테스트 코드 같은 경우는 괜찮다.
        Member result = repository.findById(member.getId()).get();

        // System.out.println 으로 결과를 출력하기 보다는
        // Assertions 을 사용하는 것이 좋다.
        // Assertions.assertEquals(member, result);
        // 위는 org.junit.jupiter.api.Assertions
        // 아래는 org.assertj.core.api -> 이것이 사용하기 더 편하다.
        assertThat(member).isEqualTo(result);
    }

    @Test
    public void findByName() {
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        Member result = repository.findByName("spring1").get();

        assertThat(result).isEqualTo(member1);
    }

    @Test
    public void findAll() {
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        List<Member> result = repository.findAll();

        assertThat(result.size()).isEqualTo(2);
    }
}
