package com.checkout.payment.gateway.domain.model.values;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class CardCvvTest {

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {" ", "abc", "12", "12345"})
  void throwsExceptionIfInvalid(String value) {
    assertThatThrownBy(() -> CardCvv.of(value))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid value.");
  }

  @Test
  void createsValid() {
    assertThat(CardCvv.of("123").value()).isEqualTo("123");
  }

  @Test
  void returnsMaskedValueAsStringRepresentation() {
    assertThat(CardCvv.of("123").toString()).isEqualTo("****");
  }

}