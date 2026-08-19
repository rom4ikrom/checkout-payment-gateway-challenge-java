package com.checkout.payment.gateway.domain.model.values;

import java.util.Objects;

abstract class PositiveInteger {

  private final int value;

  PositiveInteger(Integer value) {
    if (value == null || value <= 0) {
      throw new IllegalArgumentException("Invalid value.");
    }
    this.value = value;
  }

  public int value() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PositiveInteger that = (PositiveInteger) o;
    return value == that.value;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }
}
