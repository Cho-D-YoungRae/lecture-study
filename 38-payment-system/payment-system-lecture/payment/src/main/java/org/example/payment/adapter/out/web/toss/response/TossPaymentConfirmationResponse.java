package org.example.payment.adapter.out.web.toss.response;

import jakarta.annotation.Nullable;

import java.util.List;

public record TossPaymentConfirmationResponse(
        String version,
        String paymentKey,
        String type,
        String orderId,
        String orderName,
        String mId,
        String currency,
        String method,
        int totalAmount,
        int balanceAmount,
        String status,
        String requestedAt,
        String approvedAt,
        boolean useEscrow,
        @Nullable String lastTransactionKey,
        int suppliedAmount,
        int vat,
        boolean cultureExpense,
        int taxFreeAmount,
        int taxExemptionAmount,
        @Nullable List<Cancel> cancels,
        @Nullable Card card,
        @Nullable VirtualAccount virtualAccount,
        @Nullable MobilePhone mobilePhone,
        @Nullable GiftCertificate giftCertificate,
        @Nullable Transfer transfer,
        @Nullable Receipt receipt,
        @Nullable Checkout checkout,
        @Nullable EasyPay easyPay,
        String country,
        @Nullable TossFailureResponse failure,
        @Nullable CashReceipt cashReceipt,
        @Nullable List<CashReceipt> cashReceipts,
        @Nullable Discount discount
) {
    public record Cancel(
            int cancelAmount,
            String cancelReason,
            int taxFreeAmount,
            int taxExemptionAmount,
            int refundableAmount,
            int easyPayDiscountAmount,
            String canceledAt,
            String transactionKey,
            @Nullable String receiptKey,
            boolean isPartialCancelable
    ) {
    }

    public record Card(
            int amount,
            String issuerCode,
            @Nullable String acquirerCode,
            String number,
            int installmentPlanMonths,
            String approveNo,
            boolean useCardPoint,
            String cardType,
            String ownerType,
            String acquireStatus,
            boolean isInterestFree,
            String interestPayer
    ) {
    }

    public record VirtualAccount(
            String accountType,
            String accountNumber,
            String bankCode,
            String customerName,
            String dueDate,
            String refundStatus,
            boolean expired,
            String settlementStatus,
            RefundReceiveAccount refundReceiveAccount,
            @Nullable String secret
    ) {
    }

    public record MobilePhone(
            String customerMobilePhone,
            String settlementStatus,
            String receiptUrl
    ) {
    }

    public record GiftCertificate(
            String approveNo,
            String settlementStatus
    ) {
    }

    public record Transfer(
            String bankCode,
            String settlementStatus
    ) {
    }

    public record Receipt(
            String url
    ) {
    }

    public record Checkout(
            String url
    ) {
    }

    public record EasyPay(
            String provider,
            int amount,
            int discountAmount
    ) {
    }

    public record TossFailureResponse(
            String code,
            String message
    ) {
    }

    public record CashReceipt(
            String type,
            String receiptKey,
            String issueNumber,
            String receiptUrl,
            int amount,
            int taxFreeAmount
    ) {
    }

    public record Discount(
            int amount
    ) {
    }

    public record RefundReceiveAccount(
            String bankCode,
            String accountNumber,
            String holderName
    ) {
    }
}