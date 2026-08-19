package com.checkout.payment.gateway.domain.model.values;

public class ExpiryMonth extends PositiveInteger {

  private ExpiryMonth(Integer value) {
    super(value);
    if (value < 1 || value > 12) {
      throw new IllegalArgumentException("Invalid value.");
    }
  }

  public static ExpiryMonth of(Integer value) {
    return new ExpiryMonth(value);
  }
}
