package tobyspring.splearn.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import tobyspring.splearn.application.provided.MemberFinder;
import tobyspring.splearn.application.required.MemberRepository;
import tobyspring.splearn.domain.Member;

@Service
@Transactional
@Validated
public class MemberQueryService implements MemberFinder {

    private final MemberRepository memberRepository;

    public MemberQueryService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Member find(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다. id: " + memberId));
    }
}
