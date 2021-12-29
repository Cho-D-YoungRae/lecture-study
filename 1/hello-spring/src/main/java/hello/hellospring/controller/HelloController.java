package hello.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    // web application 에서 /hello 라고 들어오면 호출되는 method -> GET
    @GetMapping("hello")
    public String hello(Model model) {
        // template 의 data 에 "hello!!" 를 대입
        model.addAttribute("data", "hello!!");
        // hello 라는 템플릿에다가
        return "hello";
    }

    @GetMapping("hello-mvc")
    // 이전(hello)에는 직접 입력 받았는데, 이를 request parameter 로 받도록
    // @RequestParam() 의 required = true (default) -> parameter 넘겨줘야
    public String helloMVC(@RequestParam("name") String name, Model model) {
        model.addAttribute("name", name);
        return "hello-template";
    }

    // @ResponseBody: http response 의 body에 데이터를 직접 넣어주겠다.
    @GetMapping("hello-string")
    @ResponseBody
    public String helloString(@RequestParam("name") String name) {
        // 위 두 메소드와 차이
        // : 해당 view 가 받아지는 게 아니라 아래 값이 그대로 반환된다.
       return "hello " + name;
    }

    // @ResponseBody 하고 객체를 반환하면 JSON 방식으로 반환한다.
    // -> JSON 이 default 로 설정되어 있는 것
    @GetMapping("hello-api")
    @ResponseBody
    public Hello helloApi(@RequestParam("name") String name) {
        Hello hello = new Hello();
        hello.setName(name);

        return hello;
    }

    static class Hello {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
