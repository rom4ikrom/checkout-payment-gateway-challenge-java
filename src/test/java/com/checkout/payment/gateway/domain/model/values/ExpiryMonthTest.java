package com.checkout.payment.gateway.domain.model.values;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class ExpiryMonthTest {

  @ParameterizedTest
  @NullSource
  @ValueSource(ints = {0, -1, 13})
  void throwsExceptionIfInvalid(Integer value) {
    assertThatThrownBy(() -> ExpiryMonth.of(value))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid value.");
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12})
  void createsValid(int value) {
    assertThat(ExpiryMonth.of(value).value()).isEqualTo(value);
  }

}