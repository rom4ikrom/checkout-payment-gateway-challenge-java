package com.checkout.payment.gateway.presentation.model;

import lombok.Builder;

@Builder
public record PostPaymentRequest(
    // TODO: change to full card number according to task description
    int cardNumberLastFour,
    int expiryMonth,
    int expiryYear,
    String currency,
    int amount,
    int cvv
) {

}
