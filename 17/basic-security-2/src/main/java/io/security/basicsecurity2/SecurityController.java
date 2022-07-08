package io.security.basicsecurity2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {

    @GetMapping("/")
    public String index() {
        return "home";
    }

    // 1-3 Form Login 인증
    @GetMapping("/loginPage")
    public String loginPage() {
        return "loginPage";
    }

    // 1-11) 권한설정과 표현식
    @GetMapping("/user")
    public String user() {
        return "user";
    }

    // 1-11) 권한설정과 표현식
    @GetMapping("/admin/pay")
    public String adminPay() {
        return "adminPay";
    }

    // 1-11) 권한설정과 표현식
    @GetMapping("/admin/**")
    public String admin() {
        return "admin";
    }
}
