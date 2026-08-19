package com.checkout.payment.gateway.presentation.api;

import com.checkout.payment.gateway.application.service.PaymentGatewayApplicationService;
import com.checkout.payment.gateway.domain.model.payment.CardDetails;
import com.checkout.payment.gateway.domain.model.payment.Payment;
import com.checkout.payment.gateway.domain.model.values.PaymentId;
import com.checkout.payment.gateway.presentation.model.PostPaymentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.Locale;

@RestController
public class PaymentGatewayController {

  private final PaymentGatewayApplicationService paymentGatewayApplicationService;

  public PaymentGatewayController(PaymentGatewayApplicationService paymentGatewayApplicationService) {
    this.paymentGatewayApplicationService = paymentGatewayApplicationService;
  }

  @GetMapping("/payment/{id}")
  public ResponseEntity<PostPaymentResponse> getPostPaymentEventById(@PathVariable String id) {
    Payment payment = paymentGatewayApplicationService.getPaymentById(PaymentId.of(id));
    return new ResponseEntity<>(from(payment), HttpStatus.OK);
  }

  // TODO configure Jackson mapper for tiny types
  private PostPaymentResponse from(Payment payment) {
    CardDetails cardDetails = payment.cardDetails();
    return PostPaymentResponse.builder()
        .id(payment.id().value())
        .status(payment.status().name().toLowerCase(Locale.ROOT))
        .cardNumberLastFour(Integer.parseInt(cardDetails.cardNumberLastFour().value()))
        .expiryMonth(cardDetails.expiryDate().month().value())
        .expiryYear(cardDetails.expiryDate().year().value())
        .currency(payment.amount().getCurrencyUnit().getCode())
        .amount(payment.amount().getAmountMinorInt())
        .build();
  }
}
