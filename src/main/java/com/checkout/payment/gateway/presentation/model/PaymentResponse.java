package com.checkout.payment.gateway.presentation.model;

import com.checkout.payment.gateway.domain.model.payment.PaymentStatus;
import com.checkout.payment.gateway.domain.model.values.CardNumberLastFour;
import com.checkout.payment.gateway.domain.model.values.ExpiryMonth;
import com.checkout.payment.gateway.domain.model.values.ExpiryYear;
import com.checkout.payment.gateway.domain.model.values.PaymentId;
import lombok.Builder;
import org.joda.money.CurrencyUnit;

@Builder
public record PaymentResponse(
    PaymentId id,
    PaymentStatus status,
    CardNumberLastFour cardNumberLastFour,
    ExpiryMonth expiryMonth,
    ExpiryYear expiryYear,
    CurrencyUnit currency,
    int amount
) {

}
