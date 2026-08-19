package com.checkout.payment.gateway.domain.exception;

public abstract class NonFoundException extends RuntimeException {

  public NonFoundException(String message) {
    super(message);
  }
}
