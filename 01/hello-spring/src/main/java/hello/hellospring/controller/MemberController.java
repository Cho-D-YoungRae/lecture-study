package hello.hellospring.controller;

import hello.hellospring.domain.Member;
import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

// @Controller 가 있으면 스프링이 해당 객체를 생성해서 스프링 컨테이너에 갖고 있는다.
@Controller
public class MemberController {
    // 아래와 같이 생성할 수도 있지만 스프링 컨테이너에 등록해서 받아서 쓰도록 해야한다.
    // -> 해당 컨트롤러말고 다른 컨트롤러도 아래의 서비스를 가져다 쓸 수 있고,
    //      하나만 생성해도 되는 객체를 여러개 생성할 필요가 없어진다.
//    private final MemberService memberService = new MemberService();
    private final MemberService memberService;

    // @Autowired: 이 컨트롤러가 어쩃든 스프링이 시작될 때 생성되고
    //              그때 이 생성자가 호출되면서 연관된 객체를 스프링 컨테이너에서 찾아서 넣어준다
    // MemberService 를 스프링 컨테이너에 등록해야 가져올 수 있다.
    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("members/new")
    public String createForm() {
        return "members/createMemberForm";
    }

    @PostMapping("members/new")
    public String  create(MemberForm form) {
        Member member = new Member();
        member.setName(form.getName());

        memberService.join(member);

        // 끝나고 / 로 되돌아 가도록
        return "redirect:/";
    }

    @GetMapping("members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);

        return "members/memberList";
    }
}
