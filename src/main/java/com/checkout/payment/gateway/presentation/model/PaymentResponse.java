package com.checkout.payment.gateway.presentation.model;

import lombok.Builder;

@Builder
public record PaymentResponse(
    String id,
    String status,
    int cardNumberLastFour,
    int expiryMonth,
    int expiryYear,
    String currency,
    int amount
) {

}
