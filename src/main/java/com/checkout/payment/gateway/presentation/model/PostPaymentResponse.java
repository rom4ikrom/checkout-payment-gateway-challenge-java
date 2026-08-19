package com.checkout.payment.gateway.presentation.model;

import com.checkout.payment.gateway.domain.model.PaymentStatus;
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
