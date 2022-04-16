package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.argumentresolver.Login;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

import static java.util.Objects.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;

    private final SessionManager sessionManager;

//    @GetMapping("/")
    public String home() {
        return "home";
    }

    /**
     * Cookie 에 memberId 저장
     */
//    @GetMapping("/")
    public String homeLoginV1(@CookieValue(name = "memberId", required = false) Long memberId, Model model) {
        if (isNull(memberId)) {
            return "home";
        }

        Member loginMember = memberRepository.findById(memberId);
        if (isNull(loginMember)) {
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    /**
     * 직접 만든 세션 이용
     */
//    @GetMapping("/")
    public String homeLogvinV2(HttpServletRequest request, Model model) {

        // 세션 관리자에 저장된 회원 정보 조회
        Member member = (Member) sessionManager.getSession(request);

        if (isNull(member)) {
            return "home";
        }

        model.addAttribute("member", member);
        return "loginHome";
    }

    /**
     *  HttpSession 이용
     */
//    @GetMapping("/")
    public String homeLoginV3(HttpServletRequest request, Model model) {

        HttpSession session = request.getSession(false);
        if (isNull(session)) {
            return "home";
        }

        Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        if (isNull(member)) {
            return "home";
        }

        model.addAttribute("member", member);
        return "loginHome";
    }

    /**
     * Spring 의 SessionAttribute 기능 사용
     */
//    @GetMapping("/")
    public String homeLoginV3Spring(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {

        if (isNull(loginMember)) {
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    /**
     * ArgumentResolver 사용
     */
    @GetMapping("/")
    public String homeLoginV3ArgumentResolver(
            @Login Member loginMember, Model model) {

        if (isNull(loginMember)) {
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "loginHome";
    }
}