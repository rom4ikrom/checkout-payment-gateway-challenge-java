package com.checkout.payment.gateway.presentation.model;

import lombok.Builder;

@Builder
public record PostPaymentRequest(
    String cardNumber,
    int expiryMonth,
    int expiryYear,
    String currency,
    int amount,
    int cvv
) {

}
