package com.checkout.payment.gateway.application.service;

import com.checkout.payment.gateway.domain.exception.EventProcessingException;
import com.checkout.payment.gateway.domain.model.payment.Payment;
import com.checkout.payment.gateway.domain.model.values.PaymentId;
import com.checkout.payment.gateway.domain.repository.PaymentsRepository;
import com.checkout.payment.gateway.infrastructure.repository.InMemoryPaymentsRepository;
import com.checkout.payment.gateway.presentation.model.PostPaymentRequest;
import com.checkout.payment.gateway.presentation.model.PostPaymentResponse;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PaymentGatewayApplicationService {

  private static final Logger LOG = LoggerFactory.getLogger(PaymentGatewayApplicationService.class);

  private final PaymentsRepository paymentsRepository;

  public PaymentGatewayApplicationService(InMemoryPaymentsRepository paymentsRepository) {
    this.paymentsRepository = paymentsRepository;
  }

  public Payment getPaymentById(PaymentId id) {
    LOG.debug("Requesting access to to payment with ID {}", id);
    return paymentsRepository.maybePayment(id).orElseThrow(() -> new EventProcessingException("Invalid ID"));
  }
}
