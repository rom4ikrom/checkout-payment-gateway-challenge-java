package com.checkout.payment.gateway.domain.model.values;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class CardNumberTest {

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {" ", "abc", "1234567891234", "12345678912345678912", "4444 3333 2222 1111"})
  void throwsExceptionIfInvalid(String value) {
    assertThatThrownBy(() -> CardNumber.of(value))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid value.");
  }

  @Test
  void createsValid() {
    assertThat(CardNumber.of("4444333322221111").value()).isEqualTo("4444333322221111");
  }

  @Test
  void returnsMaskedValueAsStringRepresentation() {
    assertThat(CardNumber.of("4444333322221111").toString()).isEqualTo("****");
  }

  @Test
  void returnsLastFourDigits() {
    assertThat(CardNumber.of("4444333322221234").lastFour()).isEqualTo(CardNumberLastFour.of("1234"));
  }

}