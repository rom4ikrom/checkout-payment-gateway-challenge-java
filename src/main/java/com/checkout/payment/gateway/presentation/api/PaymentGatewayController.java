package com.checkout.payment.gateway.presentation.api;

import com.checkout.payment.gateway.presentation.model.PostPaymentResponse;
import com.checkout.payment.gateway.application.service.PaymentGatewayApplicationService;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentGatewayController {

  private final PaymentGatewayApplicationService paymentGatewayApplicationService;

  public PaymentGatewayController(PaymentGatewayApplicationService paymentGatewayApplicationService) {
    this.paymentGatewayApplicationService = paymentGatewayApplicationService;
  }

  @GetMapping("/payment/{id}")
  public ResponseEntity<PostPaymentResponse> getPostPaymentEventById(@PathVariable UUID id) {
    return new ResponseEntity<>(paymentGatewayApplicationService.getPaymentById(id), HttpStatus.OK);
  }
}
