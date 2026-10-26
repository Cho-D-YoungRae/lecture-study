package io.security.oauth2.springsecurityoauth2practice.sec1springsecurityfundamentals.cors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CorsController {

    @GetMapping("/users")
    public User users() {
        return new User("user", 20);
    }
}
