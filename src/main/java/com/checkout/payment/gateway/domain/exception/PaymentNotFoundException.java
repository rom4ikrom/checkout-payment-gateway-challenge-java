package com.checkout.payment.gateway.domain.exception;

import com.checkout.payment.gateway.domain.model.values.PaymentId;

public class PaymentNotFoundException extends NonFoundException {

  public PaymentNotFoundException(PaymentId id) {
    super("Payment %s was not found.".formatted(id));
  }

}
