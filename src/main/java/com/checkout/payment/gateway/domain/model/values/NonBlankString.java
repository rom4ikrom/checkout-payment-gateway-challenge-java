package com.checkout.payment.gateway.domain.model.values;

import org.apache.commons.lang3.StringUtils;
import java.util.Objects;

abstract class NonBlankString {

  private final String value;

  NonBlankString(String value) {
    if (StringUtils.isBlank(value)) {
      throw new IllegalArgumentException("Invalid value.");
    }
    this.value = value;
  }

  public String value() {
    return value;
  }

  @Override
  public String toString() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NonBlankString that = (NonBlankString) o;
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }

}
