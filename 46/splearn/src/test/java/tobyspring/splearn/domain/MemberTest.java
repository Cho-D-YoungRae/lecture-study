package tobyspring.splearn.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberTest {


    /**
     * 테스트에서는 var 를 많이 사용하기도 함.
     * 코드를 간결하게 하여 다른 중요한 것들에 더 시선이 갈 수 있도록
     */
    @Test
    void createMember() {
        var member = new Member("cho@splearn.app", "cho", "secret");

        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    /**
     * 보통 코드만 보고 직관적으로 알 수 있는 것들은 테스트를 꼭 만들지는 않지만 학습용으로 만듦
     */
    @Test
    void constructorNullCheck() {
        assertThatThrownBy(() -> new Member(null, "Toby", "secret"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void activate() {
        var member = new Member("cho@splearn.app", "cho", "secret");

        member.activate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    }

    /**
     * 커스텀 예외 VS 기본 예외
     * - 의미있는 비즈니스 예외가 있는 경우는 커스텀 예외를 사용하는 것이 좋다.
     * - 단순히 버그인 경우는 자바 기본 예외를 사용해도 좋다.
     *
     * 무의미한 코드(등록 대기가 아닐 때 등록을 하는 경우)여서 그냥 놔두어도 되지 않을까?
     * -> 버그가 될 수 있음
     */
    @Test
    void activateFail() {
        var member = new Member("cho@splearn.app", "cho", "secret");
        member.activate();

        assertThatThrownBy(() -> member.activate())
                .isInstanceOf(IllegalStateException.class);
    }
    
    @Test
    void deactivate() {
        var member = new Member("cho@splearn.app", "cho", "secret");
        member.activate();

        member.deactivate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
    }

    /**
     * 원래는 2개의 테스트로 보고 쪼개는 것이 맞긴함.
     * 하지만 간단한 것은 한 개에 넣기도함.
     */
    @Test
    void deactivateFail() {
        var member = new Member("cho@splearn.app", "cho", "secret");

        assertThatThrownBy(member::deactivate)
                .isInstanceOf(IllegalStateException.class);

        member.activate();
        member.deactivate();

        assertThatThrownBy(member::deactivate)
                .isInstanceOf(IllegalStateException.class);
    }
}