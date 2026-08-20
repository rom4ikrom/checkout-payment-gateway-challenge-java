package com.checkout.payment.gateway.acceptance;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.checkout.payment.gateway.acceptance.PaymentGatewayAcceptanceTest.TestApplicationConfiguration;
import com.checkout.payment.gateway.presentation.model.PostPaymentRequest;
import com.jayway.jsonpath.JsonPath;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
class PaymentGatewayAcceptanceTest {

  @Autowired
  private MockMvc mockMvc;
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

  @ParameterizedTest(quoteTextArguments = false)
  @CsvSource({
      "4444333322221111,1111,Authorized",
      "4444333322221112,1112,Declined"
  })
  void createsAndRetrievesPayment(String cardNumber, String expectedLastFour, String expectedStatus) throws Exception {
    // given
    var request = PostPaymentRequest.builder()
        .cardNumber(cardNumber)
        .expiryMonth(9)
        .expiryYear(2026)
        .cvv(123)
        .currency("GBP")
        .amount(4201)
        .build();

    // expect
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders
        .post("/payments")
        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isString())
        .andExpect(jsonPath("$.status").value(expectedStatus))
        .andExpect(jsonPath("$.cardNumberLastFour").value(expectedLastFour))
        .andExpect(jsonPath("$.expiryMonth").value(9))
        .andExpect(jsonPath("$.expiryYear").value(2026))
        .andExpect(jsonPath("$.currency").value("GBP"))
        .andExpect(jsonPath("$.amount").value(4201))
        .andReturn();

    // and
    String paymentId = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
    mockMvc.perform(MockMvcRequestBuilders.get("/payments/" + paymentId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(paymentId))
        .andExpect(jsonPath("$.status").value(expectedStatus))
        .andExpect(jsonPath("$.cardNumberLastFour").value(expectedLastFour))
        .andExpect(jsonPath("$.expiryMonth").value(9))
        .andExpect(jsonPath("$.expiryYear").value(2026))
        .andExpect(jsonPath("$.currency").value("GBP"))
        .andExpect(jsonPath("$.amount").value(4201));
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
