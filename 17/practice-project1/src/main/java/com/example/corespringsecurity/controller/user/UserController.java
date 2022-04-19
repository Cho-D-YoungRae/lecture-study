package com.example.corespringsecurity.controller.user;

import com.example.corespringsecurity.domain.Account;
import com.example.corespringsecurity.domain.AccountDto;
import com.example.corespringsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    @GetMapping("/mypage")
    public String myPage() {
        return "user/mypage";
    }

    @GetMapping("/users")
    public String createUser() {
        return "user/login/register";
    }

    @PostMapping("/users")
    public String createUser(AccountDto accountDto) {

        Account account = Account.builder()
                .username(accountDto.getUsername())
                .password(passwordEncoder.encode(accountDto.getPassword()))
                .email(accountDto.getEmail())
                .age(accountDto.getAge())
                .role(accountDto.getRole())
                .build();

        userService.createUser(account);

        return "redirect:/";
    }
}
