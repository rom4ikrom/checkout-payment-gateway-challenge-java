package com.checkout.payment.gateway.domain.exception;

public abstract class UnprocessableException extends RuntimeException {

  public UnprocessableException(String message) {
    super(message);
  }

}
