package me.study.inflearnjavatest.member;

import me.study.inflearnjavatest.domain.Member;
import me.study.inflearnjavatest.domain.Study;

import java.util.Optional;

public interface MemberService {

    Optional<Member> findById(Long memberId);

    void validate(Long memberId);

    void notify(Study study);

    void notify(Member member);
}
