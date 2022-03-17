package com.example.security1.controller;

import com.example.security1.model.User;
import com.example.security1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // localhost:8080/
    // localhost:8080
    @GetMapping({"", "/"})
    public String index() {
        return "index";
    }

    @GetMapping("/user")
    @ResponseBody
    public String user() {
        return "user";
    }

    @GetMapping("/admin")
    @ResponseBody
    public String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    @ResponseBody
    public String manager() {
        return "manager";
    }

    // 스프링 시큐리티가 /login 요청을 낚아챔 -> 로그인 페이지로
    // SecurityConfig 생성하니까 낚아채지 않음
    @GetMapping("/loginForm")
    public String login() {
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    // 비밀번호를 암호화된 그냥 텍스트 (ex. test!) 로 하면 시큐리티로 로그인 불가능.
    // 패스워드가 암호화 되어야 시큐리티로 로그인 가능하다.
    @PostMapping("/join")
    public String join(User user) {
        System.out.println("user = " + user);
        user.setRole("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        userRepository.save(user);
        return "redirect:/loginForm";
    }

    @Secured("ROLE_ADMIN")  // 접근 권한 config 가 아니라 어노테이션으로 개별 설정 가능, 간단하게 특정 메서드 하나에 설정하고 싶을 떄
    @GetMapping("/info")
    @ResponseBody
    public String info() {
        return "개인정보";
    }

    // @PreAuthorize, @PostAuthorize -> 예전에는 자주 사용했으나, 최근 @Secured 가 나오면서 이것을 사용함
//    @PostAuthorize()  // 자주 사용되지 않음
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") // 아래 요청에 대한 메서드 실행하기 직전에 실행된다.
    @GetMapping("/data")
    @ResponseBody
    public String date() {
        return "data";
    }
}
