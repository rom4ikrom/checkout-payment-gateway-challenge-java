package com.checkout.payment.gateway.domain.service;

import com.checkout.payment.gateway.domain.exception.RejectedAuthorisationException;
import com.checkout.payment.gateway.domain.model.authorisation.AuthorisePaymentRequest;
import com.checkout.payment.gateway.domain.model.authorisation.AuthorisePaymentResponse;
import com.checkout.payment.gateway.domain.model.payment.Payment;
import com.google.common.collect.MoreCollectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RequiredArgsConstructor
public class PaymentFactory {

  @NonNull
  private final List<PaymentStrategy> strategies;

  public Payment create(AuthorisePaymentRequest request, AuthorisePaymentResponse response) {
    return strategies.stream()
        .filter(strategy -> strategy.supports(response))
        .collect(MoreCollectors.toOptional())
        .map(strategy -> strategy.create(request, response))
        .orElseThrow(RejectedAuthorisationException::new);
  }

}
