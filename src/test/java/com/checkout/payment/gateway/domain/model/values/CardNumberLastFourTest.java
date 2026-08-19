package com.checkout.payment.gateway.domain.model.values;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class CardNumberLastFourTest {

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {" ", "abcd", "123", "12345"})
  void throwsExceptionIfInvalid(String value) {
    assertThatThrownBy(() -> CardNumberLastFour.of(value))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid value.");
  }

  @Test
  void createsValid() {
    assertThat(CardNumberLastFour.of("1234").value()).isEqualTo("1234");
  }
}