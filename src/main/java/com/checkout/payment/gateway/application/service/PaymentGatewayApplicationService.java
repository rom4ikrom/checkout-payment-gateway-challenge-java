package com.checkout.payment.gateway.application.service;

import com.checkout.payment.gateway.domain.exception.EventProcessingException;
import com.checkout.payment.gateway.domain.model.authorisation.AuthorisePaymentRequest;
import com.checkout.payment.gateway.domain.model.payment.Payment;
import com.checkout.payment.gateway.domain.model.values.PaymentId;
import com.checkout.payment.gateway.domain.repository.PaymentsRepository;
import com.checkout.payment.gateway.domain.service.PaymentsService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentGatewayApplicationService {

  private static final Logger LOG = LoggerFactory.getLogger(PaymentGatewayApplicationService.class);

  @NonNull
  private final PaymentsRepository paymentsRepository;
  @NonNull
  private final PaymentsService paymentService;

  public Payment getPaymentById(PaymentId id) {
    LOG.debug("Requesting access to to payment with ID {}", id);
    return paymentsRepository.maybePayment(id).orElseThrow(() -> new EventProcessingException("Invalid ID"));
  }

  public Payment createPayment(AuthorisePaymentRequest request) {
    return paymentService.create(request);
  }
}
