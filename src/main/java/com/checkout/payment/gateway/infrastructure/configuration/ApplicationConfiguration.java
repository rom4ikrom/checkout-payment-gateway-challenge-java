package com.checkout.payment.gateway.infrastructure.configuration;

import com.checkout.payment.gateway.domain.model.payment.Payment;
import com.checkout.payment.gateway.domain.service.AuthorisedPaymentStrategy;
import com.checkout.payment.gateway.domain.service.DeclinedPaymentStrategy;
import com.checkout.payment.gateway.domain.service.PaymentFactory;
import com.checkout.payment.gateway.domain.service.PaymentIdGenerator;
import com.checkout.payment.gateway.domain.service.PaymentStrategy;
import com.checkout.payment.gateway.domain.service.PaymentsService;
import com.checkout.payment.gateway.infrastructure.api.BankAuthorisationApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.Clock;
import java.util.List;

@Configuration
public class ApplicationConfiguration {

  @Bean
  public Clock clock() {
    return Clock.systemUTC();
  }

  @Bean
  public PaymentIdGenerator paymentIdGenerator() {
    return new PaymentIdGenerator();
  }

  @Bean
  public AuthorisedPaymentStrategy authorisedPaymentStrategy(PaymentIdGenerator paymentIdGenerator) {
    return new AuthorisedPaymentStrategy(paymentIdGenerator);
  }

  @Bean
  public DeclinedPaymentStrategy declinedPaymentStrategy(PaymentIdGenerator paymentIdGenerator) {
    return new DeclinedPaymentStrategy(paymentIdGenerator);
  }

  @Bean
  public PaymentFactory paymentFactory(List<PaymentStrategy> strategies) {
    return new PaymentFactory(strategies);
  }

  @Bean
  public PaymentsService paymentsService(
      Clock clock,
      BankAuthorisationApi bankAuthorisationApi,
      PaymentFactory paymentFactory) {
    return new PaymentsService(clock, bankAuthorisationApi, paymentFactory);
  }

}
