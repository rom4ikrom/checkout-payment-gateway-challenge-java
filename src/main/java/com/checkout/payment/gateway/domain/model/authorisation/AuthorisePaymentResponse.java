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

    private static final Declined INSTANCE = new Declined();

    private Declined() {}

    public static Declined instance() {
      return INSTANCE;
    }

  }

  final class Rejected implements AuthorisePaymentResponse {

    private static final Rejected INSTANCE = new Rejected();

    private Rejected() {}

    public static Rejected instance() {
      return INSTANCE;
    }

  }

}
