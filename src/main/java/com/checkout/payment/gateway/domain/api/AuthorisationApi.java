package com.checkout.payment.gateway.domain.api;

import com.checkout.payment.gateway.domain.model.authorisation.AuthorisePaymentRequest;
import com.checkout.payment.gateway.domain.model.authorisation.AuthorisePaymentResponse;

public interface AuthorisationApi {

  AuthorisePaymentResponse authorisePayment(AuthorisePaymentRequest request);

}
