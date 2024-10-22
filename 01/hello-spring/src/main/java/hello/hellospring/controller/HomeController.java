package hello.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    // localhost:8080 요청이 오면 "/" 에 대한 매핑이 있는지 먼저 찾는다.
    // 없으면 static 에서 index.html 을 찾는다.
    @GetMapping("/")
    public String home() {
        return "home";
    }
}
