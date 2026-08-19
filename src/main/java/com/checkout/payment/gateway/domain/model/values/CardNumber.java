package com.checkout.payment.gateway.domain.model.values;

import org.apache.commons.lang3.StringUtils;

public class CardNumber extends NonBlankString {

  private CardNumber(String value) {
    super(value);
    if (!isValid(value)) {
      throw new IllegalArgumentException("Invalid value.");
    }
  }

  private boolean isValid(String value) {
    return StringUtils.isNumeric(value) && value.length() >= 14 && value.length() <= 19;
  }

  public static CardNumber of(String value) {
    return new CardNumber(value);
  }

  @Override
  public String toString() {
    return "****";
  }

}
