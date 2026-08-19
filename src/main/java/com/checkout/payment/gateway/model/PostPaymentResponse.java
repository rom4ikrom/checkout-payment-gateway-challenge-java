package com.checkout.payment.gateway.model;

import com.checkout.payment.gateway.enums.PaymentStatus;
import lombok.Builder;
import java.util.UUID;

@Builder
public record PostPaymentResponse(
    UUID id,
    PaymentStatus status,
    int cardNumberLastFour,
    int expiryMonth,
    int expiryYear,
    String currency,
    int amount
) {

}
