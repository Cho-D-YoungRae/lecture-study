package org.example.payment.adapter.in.web.view;

import org.example.common.WebAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

@WebAdapter
@Controller
public class CheckoutController {

    @GetMapping("/")
    public Mono<String> checkoutPage() {
        return Mono.just("checkout");
    }
}
