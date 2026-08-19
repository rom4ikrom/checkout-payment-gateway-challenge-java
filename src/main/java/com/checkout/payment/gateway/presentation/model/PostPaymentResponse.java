package com.checkout.payment.gateway.presentation.model;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record PostPaymentResponse(
    @NonNull
    String id,
    @NonNull
    String status,
    int cardNumberLastFour,
    int expiryMonth,
    int expiryYear,
    @NonNull
    String currency,
    int amount
) {

}
