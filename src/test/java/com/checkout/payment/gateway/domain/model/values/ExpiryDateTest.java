package com.checkout.payment.gateway.domain.model.values;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ExpiryDateTest {

  private static final Clock CLOCK = Clock.fixed(
      Instant.parse("2026-08-19T16:13:01Z"),
      ZoneOffset.UTC);

  @ParameterizedTest
  @CsvSource({"9,2026", "5,2027"})
  void isInFuture(int month, int year) {
    assertThat(new ExpiryDate(ExpiryMonth.of(month), ExpiryYear.of(year)).inFuture(CLOCK)).isTrue();
  }

  @ParameterizedTest
  @CsvSource({"8,2026", "5,2025"})
  void isNotInFuture(int month, int year) {
    assertThat(new ExpiryDate(ExpiryMonth.of(month), ExpiryYear.of(year)).notInFuture(CLOCK)).isTrue();
  }

}