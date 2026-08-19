package com.checkout.payment.gateway.domain.exception;

import org.joda.money.CurrencyUnit;

public class UnsupportedCurrencyException extends RuntimeException {

  public UnsupportedCurrencyException(CurrencyUnit unsupportedCurrency) {
    super("Currency %s is not supported.".formatted(unsupportedCurrency));
  }

}
