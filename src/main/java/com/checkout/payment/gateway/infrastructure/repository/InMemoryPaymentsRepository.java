package com.checkout.payment.gateway.infrastructure.repository;

import com.checkout.payment.gateway.domain.repository.PaymentsRepository;
import com.checkout.payment.gateway.presentation.model.PostPaymentResponse;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryPaymentsRepository implements PaymentsRepository {

  private final HashMap<UUID, PostPaymentResponse> payments = new HashMap<>();

  public void add(PostPaymentResponse payment) {
    payments.put(payment.id(), payment);
  }

  public Optional<PostPaymentResponse> get(UUID id) {
    return Optional.ofNullable(payments.get(id));
  }

}
