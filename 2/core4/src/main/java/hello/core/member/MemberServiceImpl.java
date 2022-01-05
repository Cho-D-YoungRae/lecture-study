package hello.core.member;

public class MemberServiceImpl implements MemberService {

    // MemberRepository interface 만 남고 이제 구현체에 대한 것 없어졌다.
    // -> DIP 를 지킴.
    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
