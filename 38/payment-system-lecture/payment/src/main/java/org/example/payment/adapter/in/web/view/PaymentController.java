package org.example.payment.adapter.in.web.view;

import org.example.common.WebAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@WebAdapter
@Controller
public class PaymentController {

    @GetMapping("/success")
    public String successPage() {
        return "success";
    }

    @GetMapping("/fail")
    public String failPage() {
        return "fail";
    }
}
