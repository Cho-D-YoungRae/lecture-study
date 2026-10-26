package org.example.payment.adapter.in.web.view;

import lombok.RequiredArgsConstructor;
import org.example.common.IdempotencyCreator;
import org.example.common.WebAdapter;
import org.example.payment.application.port.in.CheckoutCommand;
import org.example.payment.application.port.in.CheckoutUseCase;
import org.example.payment.domain.CheckoutResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@WebAdapter
@Controller
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutUseCase checkoutUseCase;

    @GetMapping("/")
    public String checkoutPage(CheckoutRequest checkoutRequest, Model model) {
        CheckoutCommand checkoutCommand = new CheckoutCommand(
                checkoutRequest.cartId(),
                checkoutRequest.buyerId(),
                checkoutRequest.productIds(),
                IdempotencyCreator.create(checkoutRequest)
        );

        CheckoutResult checkoutResult = checkoutUseCase.checkout(checkoutCommand);
        model.addAttribute("orderId", checkoutResult.orderId());
        model.addAttribute("orderName", checkoutResult.orderName());
        model.addAttribute("amount", checkoutResult.amount());
        return "checkout";
    }
}
