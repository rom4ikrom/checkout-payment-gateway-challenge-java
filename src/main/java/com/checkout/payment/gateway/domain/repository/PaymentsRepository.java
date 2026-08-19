package com.checkout.payment.gateway.domain.repository;

import com.checkout.payment.gateway.domain.model.payment.Payment;
import com.checkout.payment.gateway.domain.model.values.PaymentId;
import java.util.Optional;

public interface PaymentsRepository {

  void store(Payment payment);

  Optional<Payment> maybePayment(PaymentId paymentId);

}
