package tobyspring.hellospring.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Payment {
    private Long orderId;
    private String currency;
    private BigDecimal foreignCurrencyAmount;
    private BigDecimal exRate;
    private BigDecimal convertedAmount;
    private LocalDateTime validUntil;

    public Payment(
            Long orderId,
            String currency,
            BigDecimal foreignCurrencyAmount,
            BigDecimal exRate,
            BigDecimal convertedAmount,
            LocalDateTime validUntil
    ) {
        this.convertedAmount = convertedAmount;
        this.currency = currency;
        this.exRate = exRate;
        this.foreignCurrencyAmount = foreignCurrencyAmount;
        this.orderId = orderId;
        this.validUntil = validUntil;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getExRate() {
        return exRate;
    }

    public BigDecimal getForeignCurrencyAmount() {
        return foreignCurrencyAmount;
    }

    public Long getOrderId() {
        return orderId;
    }

    public LocalDateTime getValidUntil() {
        return validUntil;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "convertedAmount=" + convertedAmount +
                ", orderId=" + orderId +
                ", currency='" + currency + '\'' +
                ", foreignCurrencyAmount=" + foreignCurrencyAmount +
                ", exRate=" + exRate +
                ", validUntil=" + validUntil +
                '}';
    }
}
