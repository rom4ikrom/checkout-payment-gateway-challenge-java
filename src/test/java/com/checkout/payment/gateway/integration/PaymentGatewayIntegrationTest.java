package com.checkout.payment.gateway.integration;

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
import com.checkout.payment.gateway.domain.repository.PaymentsRepository;
import com.checkout.payment.gateway.integration.PaymentGatewayIntegrationTest.TestApplicationConfiguration;
import com.checkout.payment.gateway.presentation.model.PostPaymentRequest;
import com.jayway.jsonpath.JsonPath;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import tools.jackson.databind.json.JsonMapper;

@SpringBootTest(
    properties = "spring.main.allow-bean-definition-overriding=true"
)
@AutoConfigureMockMvc
@Import(TestApplicationConfiguration.class)
@Testcontainers
class PaymentGatewayIntegrationTest {

  @Autowired
  private MockMvc mvc;
  @Autowired
  private PaymentsRepository paymentsRepository;
  @Autowired
  private JsonMapper objectMapper;

  @Container
  static final MountebankContainer BANK_SIMULATOR = MountebankContainer.instance();

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add(
        "clients.bank-authorisation.url",
        BANK_SIMULATOR::url
    );
  }

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
    mvc.perform(MockMvcRequestBuilders.get("/payments/" + payment.id()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("authorized"))
        .andExpect(jsonPath("$.cardNumberLastFour").value(1111))
        .andExpect(jsonPath("$.expiryMonth").value(9))
        .andExpect(jsonPath("$.expiryYear").value(2026))
        .andExpect(jsonPath("$.currency").value("GBP"))
        .andExpect(jsonPath("$.amount").value(4201));
  }

  @Test
  void createsPayment() throws Exception {
    // given
    var request = PostPaymentRequest.builder()
        .cardNumber("4444333322221111")
        .expiryMonth(9)
        .expiryYear(2026)
        .cvv(123)
        .currency("GBP")
        .amount(4201)
        .build();

    // expect
    MvcResult result = mvc.perform(MockMvcRequestBuilders
        .post("/payments")
        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isString())
        .andExpect(jsonPath("$.status").value("authorized"))
        .andExpect(jsonPath("$.cardNumberLastFour").value(1111))
        .andExpect(jsonPath("$.expiryMonth").value(9))
        .andExpect(jsonPath("$.expiryYear").value(2026))
        .andExpect(jsonPath("$.currency").value("GBP"))
        .andExpect(jsonPath("$.amount").value(4201))
        .andReturn();

    // and
    String paymentId = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
    mvc.perform(MockMvcRequestBuilders.get("/payments/" + paymentId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(paymentId));
  }

  @TestConfiguration
  static class TestApplicationConfiguration {

    @Bean
    @Primary
    public Clock clock() {
      return Clock.fixed(Instant.parse("2026-08-19T19:22:01Z"), ZoneOffset.UTC);
    }

  }
}
