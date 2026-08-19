package com.checkout.payment.gateway.domain.api;

import com.checkout.payment.gateway.domain.model.authorisation.AuthorisePaymentCommand;
import com.checkout.payment.gateway.domain.model.authorisation.AuthorisePaymentResponse;

public interface AuthorisationApi {

  AuthorisePaymentResponse authorisePayment(AuthorisePaymentCommand command);

}
