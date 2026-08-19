package com.checkout.payment.gateway.domain.model.values;

public class PaymentId extends NonBlankString {

  private PaymentId(String value) {
    super(value);
  }

  public static PaymentId of(String value) {
    return new PaymentId(value);
  }
}
