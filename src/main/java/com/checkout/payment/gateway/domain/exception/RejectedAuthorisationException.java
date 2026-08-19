package com.checkout.payment.gateway.domain.exception;

public class RejectedAuthorisationException extends UnprocessableException {

  public RejectedAuthorisationException() {
    super("Failed to authorise payment, authorisation rejected. Please try again later or use different card.");
  }
}
