package com.checkout.payment.gateway.domain.model.values;

import org.apache.commons.lang3.StringUtils;

public class CardCvv extends NonBlankString {

  private CardCvv(String value) {
    super(value);
    if (!isValid(value)) {
      throw new IllegalArgumentException("Invalid value.");
    }
  }

  private boolean isValid(String value) {
    return StringUtils.isNumeric(value) && value.length() >= 3 && value.length() <= 4;
  }

  public static CardCvv of(String value) {
    return new CardCvv(value);
  }

  @Override
  public String toString() {
    return "****";
  }

}
