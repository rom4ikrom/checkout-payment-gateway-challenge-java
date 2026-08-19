package com.checkout.payment.gateway.domain.model.values;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NonBlankStringTest {

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {" "})
  void throwsExceptionIfInvalid(String value) {
    assertThatThrownBy(() -> new TestNonBlankString(value))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid value.");
  }

  @Test
  void equalsByValueAndConcreteType() {
    assertThat(new TestNonBlankString("abc")).isEqualTo(new TestNonBlankString("abc"));
    assertThat(new TestNonBlankString("abc")).isNotEqualTo(new OtherNonBlankString("abc"));
  }

  static class TestNonBlankString extends NonBlankString {
    TestNonBlankString(String value) {
      super(value);
    }
  }

  static class OtherNonBlankString extends NonBlankString {
    OtherNonBlankString(String value) {
      super(value);
    }
  }

}