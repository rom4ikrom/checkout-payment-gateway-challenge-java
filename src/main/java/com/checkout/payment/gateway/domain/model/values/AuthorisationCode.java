package com.checkout.payment.gateway.domain.model.values;

public class AuthorisationCode extends NonBlankString {

  private AuthorisationCode(String value) {
    super(value);
  }

  public static AuthorisationCode of(String value) {
    return new AuthorisationCode(value);
  }
}
