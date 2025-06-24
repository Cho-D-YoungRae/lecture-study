package tobyspring.splearn.application;

import org.springframework.stereotype.Service;
import tobyspring.splearn.application.provided.MemberRegister;
import tobyspring.splearn.application.required.EmailSender;
import tobyspring.splearn.application.required.MemberRepository;
import tobyspring.splearn.domain.Member;
import tobyspring.splearn.domain.MemberRegisterRequest;
import tobyspring.splearn.domain.PasswordEncoder;

@Service
public class MemberService implements MemberRegister {

    private final MemberRepository memberRepository;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;

    public MemberService(
            MemberRepository memberRepository,
            EmailSender emailSender,
            PasswordEncoder passwordEncoder
    ) {
        this.memberRepository = memberRepository;
        this.emailSender = emailSender;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Member register(MemberRegisterRequest registerRequest) {
        Member member = memberRepository.save(
                Member.register(registerRequest, passwordEncoder)
        );

        emailSender.send(
                member.getEmail(),
                "등록을 완료해주세요.",
                "아래 링크를 클릭해서 등록을 완료해주세요."
        );
        return member;
    }
}
