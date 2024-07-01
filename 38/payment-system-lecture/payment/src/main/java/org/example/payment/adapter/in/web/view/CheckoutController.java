package org.example.payment.adapter.in.web.view;

import lombok.RequiredArgsConstructor;
import org.example.common.IdempotencyCreator;
import org.example.common.WebAdapter;
import org.example.payment.application.port.in.CheckoutCommand;
import org.example.payment.application.port.in.CheckoutUseCase;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

@WebAdapter
@Controller
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutUseCase checkoutUseCase;

    @GetMapping("/")
    public Mono<String> checkoutPage(CheckoutRequest checkoutRequest, Model model) {
        final CheckoutCommand checkoutCommand = new CheckoutCommand(
                checkoutRequest.cartId(),
                checkoutRequest.buyerId(),
                checkoutRequest.productIds(),
                IdempotencyCreator.create(checkoutRequest)
        );

        return checkoutUseCase.checkout(checkoutCommand)
                .map(it -> {
                    model.addAttribute("orderId", it.orderId());
                    model.addAttribute("orderName", it.orderName());
                    model.addAttribute("amount", it.amount());
                    return "checkout";
                });
    }
}
