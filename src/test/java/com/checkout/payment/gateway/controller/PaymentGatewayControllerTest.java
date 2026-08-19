package com.checkout.payment.gateway.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.checkout.payment.gateway.domain.model.payment.CardDetails;
import com.checkout.payment.gateway.domain.model.payment.Payment;
import com.checkout.payment.gateway.domain.model.payment.PaymentStatus;
import com.checkout.payment.gateway.domain.model.values.CardCvv;
import com.checkout.payment.gateway.domain.model.values.CardNumber;
import com.checkout.payment.gateway.domain.model.values.ExpiryDate;
import com.checkout.payment.gateway.domain.model.values.ExpiryMonth;
import com.checkout.payment.gateway.domain.model.values.ExpiryYear;
import com.checkout.payment.gateway.domain.model.values.PaymentId;
import com.checkout.payment.gateway.infrastructure.repository.InMemoryPaymentsRepository;
import java.math.BigDecimal;
import java.util.UUID;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentGatewayControllerTest {

  @Autowired
  private MockMvc mvc;
  @Autowired
  InMemoryPaymentsRepository paymentsRepository;

  @Test
  void whenPaymentWithIdExistThenCorrectPaymentIsReturned() throws Exception {
    // given
    var payment = Payment.builder()
        .id(PaymentId.of("cc837a9b-e493-4ceb-a752-7f5aba8e1d86"))
        .cardDetails(new CardDetails(
            CardNumber.of("4444333322221111"),
            new ExpiryDate(ExpiryMonth.of(9), ExpiryYear.of(2026)),
            CardCvv.of("123")))
        .status(PaymentStatus.AUTHORIZED)
        .amount(Money.of(CurrencyUnit.GBP, new BigDecimal("42.01")))
        .build();
    paymentsRepository.store(payment);

    // expect
    mvc.perform(MockMvcRequestBuilders.get("/payment/" + payment.id()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("authorized"))
        .andExpect(jsonPath("$.cardNumberLastFour").value(1111))
        .andExpect(jsonPath("$.expiryMonth").value(9))
        .andExpect(jsonPath("$.expiryYear").value(2026))
        .andExpect(jsonPath("$.currency").value("GBP"))
        .andExpect(jsonPath("$.amount").value(4201));
  }

  @Test
  void whenPaymentWithIdDoesNotExistThen404IsReturned() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/payment/" + UUID.randomUUID()))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Page not found"));
  }
}
