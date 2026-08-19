package com.checkout.payment.gateway.presentation.api;

import com.checkout.payment.gateway.application.service.PaymentGatewayApplicationService;
import com.checkout.payment.gateway.domain.model.authorisation.AuthorisePaymentRequest;
import com.checkout.payment.gateway.domain.model.payment.CardDetails;
import com.checkout.payment.gateway.domain.model.payment.Payment;
import com.checkout.payment.gateway.domain.model.values.CardCvv;
import com.checkout.payment.gateway.domain.model.values.CardNumber;
import com.checkout.payment.gateway.domain.model.values.ExpiryDate;
import com.checkout.payment.gateway.domain.model.values.ExpiryMonth;
import com.checkout.payment.gateway.domain.model.values.ExpiryYear;
import com.checkout.payment.gateway.domain.model.values.PaymentId;
import com.checkout.payment.gateway.presentation.model.PaymentResponse;
import com.checkout.payment.gateway.presentation.model.PostPaymentRequest;
import org.joda.money.CurrencyUnit;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentGatewayController {

  private final PaymentGatewayApplicationService paymentGatewayApplicationService;

  public PaymentGatewayController(PaymentGatewayApplicationService paymentGatewayApplicationService) {
    this.paymentGatewayApplicationService = paymentGatewayApplicationService;
  }

  @GetMapping("/payments/{id}")
  public ResponseEntity<PaymentResponse> getPayment(@PathVariable String id) {
    Payment payment = paymentGatewayApplicationService.getPaymentById(PaymentId.of(id));
    return new ResponseEntity<>(from(payment), HttpStatus.OK);
  }

  @PostMapping("/payments")
  public ResponseEntity<PaymentResponse> createPayment(@RequestBody PostPaymentRequest request) {
    Payment payment = paymentGatewayApplicationService.createPayment(from(request));
    return new ResponseEntity<>(from(payment), HttpStatus.CREATED);
  }

  private PaymentResponse from(Payment payment) {
    CardDetails cardDetails = payment.cardDetails();
    return PaymentResponse.builder()
        .id(payment.id())
        .status(payment.status())
        .cardNumberLastFour(cardDetails.cardNumberLastFour())
        .expiryMonth(cardDetails.expiryDate().month())
        .expiryYear(cardDetails.expiryDate().year())
        .currency(payment.amount().getCurrencyUnit())
        .amount(payment.amount().getAmountMinorInt())
        .build();
  }

  private AuthorisePaymentRequest from(PostPaymentRequest request) {
    return AuthorisePaymentRequest.builder()
        .cardNumber(CardNumber.of(request.cardNumber()))
        .expiryDate(new ExpiryDate(ExpiryMonth.of(request.expiryMonth()), ExpiryYear.of(request.expiryYear())))
        .cardCvv(CardCvv.of(String.valueOf(request.cvv())))
        .currency(CurrencyUnit.of(request.currency()))
        .amount(request.amount())
        .build();
  }

}
