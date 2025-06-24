package tobyspring.splearn.application.member.provided;

import jakarta.validation.Valid;
import tobyspring.splearn.domain.member.Member;
import tobyspring.splearn.domain.member.MemberRegisterRequest;

/**
 * 회원의 등록과 관련한 기능을 제공한다.
 */
public interface MemberRegister {

    Member register(@Valid MemberRegisterRequest registerRequest);

    Member activate(Long memberId);
}
