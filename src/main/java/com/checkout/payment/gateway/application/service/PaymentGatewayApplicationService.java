package com.checkout.payment.gateway.application.service;

import com.checkout.payment.gateway.domain.exception.PaymentNotFoundException;
import com.checkout.payment.gateway.domain.model.authorisation.AuthorisePaymentRequest;
import com.checkout.payment.gateway.domain.model.payment.Payment;
import com.checkout.payment.gateway.domain.model.values.PaymentId;
import com.checkout.payment.gateway.domain.repository.PaymentsRepository;
import com.checkout.payment.gateway.domain.service.PaymentsService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentGatewayApplicationService {

  @NonNull
  private final PaymentsRepository paymentsRepository;
  @NonNull
  private final PaymentsService paymentService;

  public Payment getPaymentById(PaymentId id) {
    return paymentsRepository.maybePayment(id).orElseThrow(() -> new PaymentNotFoundException(id));
  }

  public Payment createPayment(AuthorisePaymentRequest request) {
    Payment payment = paymentService.create(request);
    paymentsRepository.store(payment);
    return payment;
  }
}
