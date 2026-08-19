package com.checkout.payment.gateway.domain.service;

import com.checkout.payment.gateway.domain.model.authorisation.AuthorisePaymentRequest;
import com.checkout.payment.gateway.domain.model.authorisation.AuthorisePaymentResponse;
import com.checkout.payment.gateway.domain.model.payment.CardDetails;
import com.checkout.payment.gateway.domain.model.payment.Payment;
import com.checkout.payment.gateway.domain.model.payment.PaymentStatus;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.joda.money.Money;
import java.math.BigDecimal;

@RequiredArgsConstructor
public class DeclinedPaymentStrategy implements PaymentStrategy {

  @NonNull
  private final PaymentIdGenerator paymentIdGenerator;

  @Override
  public boolean supports(AuthorisePaymentResponse response) {
    return response instanceof AuthorisePaymentResponse.Declined;
  }

  @Override
  public Payment create(AuthorisePaymentRequest request, AuthorisePaymentResponse response) {
    return Payment.builder()
        .id(paymentIdGenerator.nextId())
        .cardDetails(new CardDetails(request.cardNumber(), request.expiryDate(), request.cardCvv()))
        .status(PaymentStatus.DECLINED)
        .amount(Money.ofMinor(request.currency(), request.amount()))
        .build();

  }
}
