package com.checkout.payment.gateway.presentation.api;

import static com.checkout.payment.gateway.domain.utils.TestFixtures.validAuthorisePaymentRequest;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.checkout.payment.gateway.application.service.PaymentGatewayApplicationService;
import com.checkout.payment.gateway.domain.exception.PastYearMonthException;
import com.checkout.payment.gateway.domain.exception.PaymentNotFoundException;
import com.checkout.payment.gateway.domain.exception.RejectedAuthorisationException;
import com.checkout.payment.gateway.domain.exception.UnprocessableException;
import com.checkout.payment.gateway.domain.exception.UnsupportedCurrencyException;
import com.checkout.payment.gateway.domain.model.payment.CardDetails;
import com.checkout.payment.gateway.domain.model.payment.Payment;
import com.checkout.payment.gateway.domain.model.payment.PaymentStatus;
import com.checkout.payment.gateway.domain.model.values.CardCvv;
import com.checkout.payment.gateway.domain.model.values.CardNumber;
import com.checkout.payment.gateway.domain.model.values.ExpiryDate;
import com.checkout.payment.gateway.domain.model.values.ExpiryMonth;
import com.checkout.payment.gateway.domain.model.values.ExpiryYear;
import com.checkout.payment.gateway.domain.model.values.PaymentId;
import com.checkout.payment.gateway.infrastructure.configuration.JacksonConfiguration;
import com.checkout.payment.gateway.presentation.model.PostPaymentRequest;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.stream.Stream;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tools.jackson.databind.json.JsonMapper;

@WebMvcTest(PaymentGatewayController.class)
@Import(JacksonConfiguration.class)
class PaymentGatewayControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private JsonMapper jsonMapper;

  @MockitoBean
  private PaymentGatewayApplicationService paymentGatewayApplicationService;

  @Test
  void returnsExistingPayment() throws Exception {
    // given
    var paymentId = PaymentId.of("cc837a9b-e493-4ceb-a752-7f5aba8e1d86");
    var payment = Payment.builder()
        .id(paymentId)
        .cardDetails(new CardDetails(
            CardNumber.of("4444333322221111"),
            new ExpiryDate(ExpiryMonth.of(9), ExpiryYear.of(2026)),
            CardCvv.of("123")))
        .status(PaymentStatus.AUTHORIZED)
        .amount(Money.of(CurrencyUnit.GBP, new BigDecimal("42.01")))
        .build();
    when(paymentGatewayApplicationService.getPaymentById(paymentId)).thenReturn(payment);

    // expect
    mockMvc.perform(MockMvcRequestBuilders.get("/payments/" + payment.id()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(paymentId.value()))
        .andExpect(jsonPath("$.status").value("Authorized"))
        .andExpect(jsonPath("$.cardNumberLastFour").value(1111))
        .andExpect(jsonPath("$.expiryMonth").value(9))
        .andExpect(jsonPath("$.expiryYear").value(2026))
        .andExpect(jsonPath("$.currency").value("GBP"))
        .andExpect(jsonPath("$.amount").value(4201));
  }

  @Test
  void returnsPaymentWhenCreated() throws Exception {
    // given
    var request = PostPaymentRequest.builder()
        .cardNumber("4444333322221111")
        .expiryMonth(9)
        .expiryYear(2026)
        .cvv(123)
        .currency("GBP")
        .amount(4201)
        .build();
    var paymentId = PaymentId.of("cc837a9b-e493-4ceb-a752-7f5aba8e1d86");
    var payment = Payment.builder()
        .id(paymentId)
        .cardDetails(new CardDetails(
            CardNumber.of("4444333322221111"),
            new ExpiryDate(ExpiryMonth.of(9), ExpiryYear.of(2026)),
            CardCvv.of("123")))
        .status(PaymentStatus.AUTHORIZED)
        .amount(Money.of(CurrencyUnit.GBP, new BigDecimal("42.01")))
        .build();
    when(paymentGatewayApplicationService.createPayment(validAuthorisePaymentRequest())).thenReturn(payment);

    // expect
    mockMvc.perform(MockMvcRequestBuilders.post("/payments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(paymentId.value()))
        .andExpect(jsonPath("$.status").value("Authorized"))
        .andExpect(jsonPath("$.cardNumberLastFour").value(1111))
        .andExpect(jsonPath("$.expiryMonth").value(9))
        .andExpect(jsonPath("$.expiryYear").value(2026))
        .andExpect(jsonPath("$.currency").value("GBP"))
        .andExpect(jsonPath("$.amount").value(4201));
  }

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
    when(paymentGatewayApplicationService.createPayment(validAuthorisePaymentRequest())).thenThrow(exception);

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