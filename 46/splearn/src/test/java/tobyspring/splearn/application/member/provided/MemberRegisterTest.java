package tobyspring.splearn.application.member.provided;

import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import tobyspring.splearn.SplearnTestConfiguration;
import tobyspring.splearn.domain.member.DuplicateEmilException;
import tobyspring.splearn.domain.member.Member;
import tobyspring.splearn.domain.member.MemberRegisterRequest;
import tobyspring.splearn.domain.member.MemberStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static tobyspring.splearn.domain.MemberFixture.createMemberRegisterRequest;

@SpringBootTest
@Import(SplearnTestConfiguration.class)
@Transactional
record MemberRegisterTest(tobyspring.splearn.application.member.provided.MemberRegister memberRegister, EntityManager entityManager) {

    @Test
    void register() {
        Member member = memberRegister.register(createMemberRegisterRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void duplicationEmailFail() {
        memberRegister.register(createMemberRegisterRequest());

        assertThatThrownBy(() -> memberRegister.register(createMemberRegisterRequest()))
                .isInstanceOf(DuplicateEmilException.class);
    }

    @Test
    void activate() {
        Member member = memberRegister.register(createMemberRegisterRequest());
        entityManager.flush();
        entityManager.clear();

        member = memberRegister.activate(member.getId());
        entityManager.flush();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
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