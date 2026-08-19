package com.checkout.payment.gateway.domain.service;

import com.checkout.payment.gateway.domain.model.authorisation.AuthorisePaymentRequest;
import com.checkout.payment.gateway.domain.model.authorisation.AuthorisePaymentResponse;
import com.checkout.payment.gateway.domain.model.payment.Payment;

public interface PaymentStrategy {

  boolean supports(AuthorisePaymentResponse response);

  Payment create(AuthorisePaymentRequest request, AuthorisePaymentResponse response);

}
