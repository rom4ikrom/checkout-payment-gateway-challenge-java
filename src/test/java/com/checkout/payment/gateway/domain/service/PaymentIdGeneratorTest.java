package com.checkout.payment.gateway.domain.service;

import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class PaymentIdGeneratorTest {

  private final PaymentIdGenerator underTest = new PaymentIdGenerator();

  @Test
  void generatedUniqueId() {
    assertThatNoException().isThrownBy(() -> UUID.fromString(underTest.nextId().value()));
  }

}