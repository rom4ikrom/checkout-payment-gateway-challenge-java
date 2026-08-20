package com.checkout.payment.gateway.domain.exception;

public class PastYearMonthException extends UnprocessableException {

  public PastYearMonthException() {
    super("Month and year must be in the future.");
  }

}
