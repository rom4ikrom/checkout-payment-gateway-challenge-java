package com.checkout.payment.gateway.presentation.api;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.checkout.payment.gateway.application.service.PaymentGatewayApplicationService;
import com.checkout.payment.gateway.domain.exception.PastYearMonthException;
import com.checkout.payment.gateway.domain.exception.PaymentNotFoundException;
import com.checkout.payment.gateway.domain.exception.RejectedAuthorisationException;
import com.checkout.payment.gateway.domain.exception.UnprocessableException;
import com.checkout.payment.gateway.domain.exception.UnsupportedCurrencyException;
import com.checkout.payment.gateway.domain.model.values.PaymentId;
import com.checkout.payment.gateway.domain.utils.TestFixtures;
import com.checkout.payment.gateway.presentation.model.PostPaymentRequest;
import java.util.UUID;
import java.util.stream.Stream;
import org.joda.money.CurrencyUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tools.jackson.databind.json.JsonMapper;

@WebMvcTest(PaymentGatewayController.class)
class PaymentGatewayControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private JsonMapper jsonMapper;

  @MockitoBean
  private PaymentGatewayApplicationService paymentGatewayApplicationService;

  @Test
  void respondsWith404WhenPaymentIsNotFound() throws Exception {
    // given
    String nonExistingPaymentId = UUID.randomUUID().toString();
    PaymentId paymentId = PaymentId.of(nonExistingPaymentId);
    when(paymentGatewayApplicationService.getPaymentById(paymentId))
        .thenThrow(new PaymentNotFoundException(paymentId));

    // expect
    mockMvc.perform(MockMvcRequestBuilders.get("/payments/" + nonExistingPaymentId))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Payment %s was not found.".formatted(nonExistingPaymentId)));
  }

  @Test
  void respondsWith400ForInvalidRequestBody() throws Exception {
    // given
    var request = PostPaymentRequest.builder()
        .cardNumber("abc")
        .expiryMonth(9)
        .expiryYear(2026)
        .cvv(123)
        .currency("GBP")
        .amount(4201)
        .build();

    // expect
    mockMvc.perform(MockMvcRequestBuilders
            .post("/payments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Invalid value."));
  }

  @ParameterizedTest(name = "{index} {1}", quoteTextArguments = false)
  @MethodSource("unprocessableExceptions")
  void respondsWith422ForUnprocessableExceptions(UnprocessableException exception, String testCaseName) throws Exception {
    // given
    var request = PostPaymentRequest.builder()
        .cardNumber("4444333322221111")
        .expiryMonth(9)
        .expiryYear(2026)
        .cvv(123)
        .currency("GBP")
        .amount(4201)
        .build();
    when(paymentGatewayApplicationService.createPayment(TestFixtures.validAuthorisePaymentRequest())).thenThrow(exception);

    // expect
    mockMvc.perform(MockMvcRequestBuilders
            .post("/payments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonMapper.writeValueAsString(request)))
        .andExpect(status().isUnprocessableContent())
        .andExpect(jsonPath("$.message").value(exception.getMessage()));
  }

  private static Stream<Arguments> unprocessableExceptions() {
    return Stream.of(
        Arguments.of(new UnsupportedCurrencyException(CurrencyUnit.of("UAH")), "UnsupportedCurrencyException"),
        Arguments.of(new RejectedAuthorisationException(), "RejectedAuthorisationException"),
        Arguments.of(new PastYearMonthException(), "PastYearMonthException")
    );
  }

}