package com.checkout.payment.gateway.infrastructure.repository;

import com.checkout.payment.gateway.domain.model.payment.Payment;
import com.checkout.payment.gateway.domain.model.values.PaymentId;
import com.checkout.payment.gateway.domain.repository.PaymentsRepository;
import com.checkout.payment.gateway.infrastructure.entity.PaymentEntity;
import com.checkout.payment.gateway.presentation.model.PostPaymentResponse;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryPaymentsRepository implements PaymentsRepository {

  private final HashMap<PaymentId, PaymentEntity> payments = new HashMap<>();

  @Override
  public void store(Payment payment) {
    PaymentEntity entity = PaymentEntity.builder()
        .id(payment.id())
        .cardDetails(payment.cardDetails())
        .status(payment.status())
        .amount(payment.amount())
        .authorisationCode(payment.authorisationCodeOrNull())
        .build();
    payments.put(entity.id(), entity);
  }

  @Override
  public Optional<Payment> maybePayment(PaymentId paymentId) {
    return Optional.ofNullable(payments.get(paymentId))
        .map(entity -> Payment.builder()
            .id(entity.id())
            .cardDetails(entity.cardDetails())
            .status(entity.status())
            .amount(entity.amount())
            .authorisationCode(entity.authorisationCodeOrNull())
            .build());
  }
}
