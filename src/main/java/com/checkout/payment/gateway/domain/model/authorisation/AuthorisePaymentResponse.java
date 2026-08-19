package com.checkout.payment.gateway.domain.model.authorisation;

import com.checkout.payment.gateway.domain.model.values.AuthorisationCode;
import lombok.NonNull;
import lombok.Value;

public sealed interface AuthorisePaymentResponse {

  @Value
  class Authorised implements AuthorisePaymentResponse {
    @NonNull
    AuthorisationCode authorisationCode;
  }

  final class Declined implements AuthorisePaymentResponse {

  }

  final class Rejected implements AuthorisePaymentResponse {

  }

}
