package com.checkout.payment.gateway.domain.model.values;

public class ExpiryYear extends PositiveInteger {

  private ExpiryYear(Integer value) {
    super(value);
  }

  public static ExpiryYear of(Integer value) {
    return new ExpiryYear(value);
  }
}
