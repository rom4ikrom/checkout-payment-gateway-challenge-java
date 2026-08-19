package com.checkout.payment.gateway.domain.service;

import com.checkout.payment.gateway.domain.model.authorisation.AuthorisePaymentRequest;
import com.checkout.payment.gateway.domain.model.authorisation.AuthorisePaymentResponse;
import com.checkout.payment.gateway.domain.model.payment.CardDetails;
import com.checkout.payment.gateway.domain.model.payment.Payment;
import com.checkout.payment.gateway.domain.model.payment.PaymentStatus;
import java.math.BigDecimal;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.joda.money.Money;

@RequiredArgsConstructor
public class AuthorisedPaymentStrategy implements PaymentStrategy {

  @NonNull
  private final PaymentIdGenerator paymentIdGenerator;

  @Override
  public boolean supports(AuthorisePaymentResponse response) {
    return response instanceof AuthorisePaymentResponse.Authorised;
  }

  @Override
  public Payment create(AuthorisePaymentRequest request, AuthorisePaymentResponse response) {
    var authorisedResponse = (AuthorisePaymentResponse.Authorised) response;
    return Payment.builder()
        .id(paymentIdGenerator.nextId())
        .cardDetails(new CardDetails(request.cardNumber(), request.expiryDate(), request.cardCvv()))
        .status(PaymentStatus.AUTHORIZED)
        .amount(Money.of(request.currency(), BigDecimal.valueOf(request.amount(), 2)))
        .authorisationCode(authorisedResponse.authorisationCode())
        .build();

  }
}
