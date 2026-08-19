package com.checkout.payment.gateway.domain.model.values;

import org.apache.commons.lang3.StringUtils;

public class CardNumberLastFour extends NonBlankString {

  private CardNumberLastFour(String value) {
    super(value);
    if (!isValid(value)) {
      throw new IllegalArgumentException("Invalid value.");
    }
  }

  public static CardNumberLastFour of(String value) {
    return new CardNumberLastFour(value);
  }

  private boolean isValid(String value) {
    return value.length() == 4 && StringUtils.isNumeric(value);
  }
}
