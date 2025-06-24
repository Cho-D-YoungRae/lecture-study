package tobyspring.splearn.application.provided;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import tobyspring.splearn.SplearnTestConfiguration;
import tobyspring.splearn.domain.DuplicateEmilException;
import tobyspring.splearn.domain.Member;
import tobyspring.splearn.domain.MemberRegisterRequest;
import tobyspring.splearn.domain.MemberStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static tobyspring.splearn.domain.MemberFixture.createMemberRegisterRequest;

@SpringBootTest
@Import(SplearnTestConfiguration.class)
@Transactional
record MemberRegisterTest(MemberRegister memberRegister) {

    @Test
    void register() {
        Member member = memberRegister.register(
                createMemberRegisterRequest()
        );

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void duplicationEmailFail() {
        Member member = memberRegister.register(
                createMemberRegisterRequest()
        );

        assertThatThrownBy(() -> memberRegister.register(createMemberRegisterRequest()))
                .isInstanceOf(DuplicateEmilException.class);
    }

    @Test
    void memberRegisterRequestFail() {

        assertThatThrownBy(() -> memberRegister.register(new MemberRegisterRequest(
                "cho@splearn.app",
                "cho",
                "secret"
        ))).isInstanceOf(ConstraintViolationException.class);

        assertThatThrownBy(() -> memberRegister.register(new MemberRegisterRequest(
                "cho@splearn.app",
                "cho--------------------------------------------------------",
                "secret"
        ))).isInstanceOf(ConstraintViolationException.class);

        assertThatThrownBy(() -> memberRegister.register(new MemberRegisterRequest(
                "chosplearn.app",
                "cho",
                "secret"
        ))).isInstanceOf(ConstraintViolationException.class);
    }
}