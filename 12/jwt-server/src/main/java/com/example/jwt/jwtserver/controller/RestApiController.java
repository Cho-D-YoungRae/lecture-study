package com.example.jwt.jwtserver.controller;

import com.example.jwt.jwtserver.model.User;
import com.example.jwt.jwtserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

//@CrossOrigin 이렇게 cors 해결할 수도 있으나 인증이 필요한 요청 모두 거부된다. 인증이 필요하지 않는 요청만 허ㅖㅐ
@RestController
@RequiredArgsConstructor
public class RestApiController {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserRepository userRepository;

    @GetMapping("/home")
    public String home() {
        return "<h1>home<h1>";
    }

    @PostMapping("/token")
    public String token() {
        return "token";
    }

    @PostMapping("/join")
    public String join(@RequestBody User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles("ROLE_USER");
        userRepository.save(user);

        return "회원가입완료";
    }

    // user, manager, admin 접근 가능
    @GetMapping("/api/v1/user")
    public String user() {
        return "user";
    }

    // manager, admin 접근 가능
    @GetMapping("/api/v1/manager")
    public String manager() {
        return "manager";
    }

    // admin 접근 가능
    @GetMapping("/api/v1/admin")
    public String admin() {
        return "admin";
    }
}