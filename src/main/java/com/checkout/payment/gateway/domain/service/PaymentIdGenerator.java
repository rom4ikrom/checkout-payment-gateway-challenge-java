package com.checkout.payment.gateway.domain.service;

import com.checkout.payment.gateway.domain.model.values.PaymentId;
import java.util.UUID;

public class PaymentIdGenerator {

  PaymentId nextId() {
    return PaymentId.of(UUID.randomUUID().toString());
  }


}
