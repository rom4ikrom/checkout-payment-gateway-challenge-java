package com.checkout.payment.gateway.domain.model.values;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class PositiveIntegerTest {

  @ParameterizedTest
  @NullSource
  @ValueSource(ints = {0, -1})
  void throwsExceptionIfInvalid(Integer value) {
    assertThatThrownBy(() -> new TestPositiveInteger(value))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid value.");
  }

  @Test
  void createsValid() {
    assertThat(new TestPositiveInteger(1).value()).isEqualTo(1);
  }

  @Test
  void equalsByValueAndConcreteType() {
    assertThat(new TestPositiveInteger(1)).isEqualTo(new TestPositiveInteger(1));
    assertThat(new TestPositiveInteger(1)).isNotEqualTo(new OtherPositiveInteger(1));
  }

  static class TestPositiveInteger extends PositiveInteger {
    TestPositiveInteger(Integer value) {
      super(value);
    }
  }

  static class OtherPositiveInteger extends PositiveInteger {
    OtherPositiveInteger(Integer value) {
      super(value);
    }
  }

}