package com.checkout.payment.gateway.infrastructure.api;

import com.checkout.payment.gateway.domain.api.AuthorisationApi;
import com.checkout.payment.gateway.domain.model.authorisation.AuthorisePaymentRequest;
import com.checkout.payment.gateway.domain.model.authorisation.AuthorisePaymentResponse;
import com.checkout.payment.gateway.domain.model.values.AuthorisationCode;
import org.springframework.stereotype.Component;

@Component
public class BankAuthorisationApi implements AuthorisationApi {

  @Override
  public AuthorisePaymentResponse authorisePayment(AuthorisePaymentRequest request) {
    return new AuthorisePaymentResponse.Authorised(AuthorisationCode.of("abc59d89-4598-4e53-949e-953da53830ce"));
  }
}
