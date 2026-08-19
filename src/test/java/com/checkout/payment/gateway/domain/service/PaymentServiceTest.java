package com.checkout.payment.gateway.domain.service;

import static com.checkout.payment.gateway.domain.utils.TestFixtures.validAuthorisePaymentRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.checkout.payment.gateway.domain.api.AuthorisationApi;
import com.checkout.payment.gateway.domain.exception.PastYearMonthException;
import com.checkout.payment.gateway.domain.exception.UnsupportedCurrencyException;
import com.checkout.payment.gateway.domain.model.authorisation.AuthorisePaymentRequest;
import com.checkout.payment.gateway.domain.model.authorisation.AuthorisePaymentResponse;
import com.checkout.payment.gateway.domain.model.payment.CardDetails;
import com.checkout.payment.gateway.domain.model.payment.Payment;
import com.checkout.payment.gateway.domain.model.payment.PaymentStatus;
import com.checkout.payment.gateway.domain.model.values.AuthorisationCode;
import com.checkout.payment.gateway.domain.model.values.CardCvv;
import com.checkout.payment.gateway.domain.model.values.CardNumber;
import com.checkout.payment.gateway.domain.model.values.ExpiryDate;
import com.checkout.payment.gateway.domain.model.values.ExpiryMonth;
import com.checkout.payment.gateway.domain.model.values.ExpiryYear;
import com.checkout.payment.gateway.domain.model.values.PaymentId;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

  private static final Clock CLOCK = Clock.fixed(
      Instant.parse("2026-08-19T16:13:01Z"),
      ZoneOffset.UTC);

  private PaymentService underTest;

  @Mock
  private AuthorisationApi authorisationApi;
  @Mock
  private PaymentFactory paymentFactory;

  @BeforeEach
  void setup() {
    underTest = new PaymentService(CLOCK, authorisationApi, paymentFactory);
  }

  @Test
  void returnsPayment() {
    // given
    var request = validAuthorisePaymentRequest();
    var response = new AuthorisePaymentResponse.Authorised(
        AuthorisationCode.of("617fcef2-c7c1-4c7d-b0ff-ff5eadc23cd6"));
    when(authorisationApi.authorisePayment(request)).thenReturn(response);

    // and
    var payment = Payment.builder()
        .id(PaymentId.of("cc837a9b-e493-4ceb-a752-7f5aba8e1d86"))
        .cardDetails(new CardDetails(
            CardNumber.of("4444333322221111"),
            new ExpiryDate(ExpiryMonth.of(9), ExpiryYear.of(2026)),
            CardCvv.of("123")))
        .status(PaymentStatus.AUTHORIZED)
        .amount(Money.of(CurrencyUnit.GBP, new BigDecimal("42.01")))
        .build();
    when(paymentFactory.create(request, response)).thenReturn(payment);

    // when
    var result = underTest.create(request);

    // then
    assertThat(result).isEqualTo(payment);
  }

  @Test
  void throwsExceptionIfCurrencyIsNotSupported() {
    // given
    var request = validAuthorisePaymentRequest().toBuilder()
        .currency(CurrencyUnit.of("UAH"))
        .build();

    // expect
    assertThatThrownBy(() -> underTest.create(request))
        .isInstanceOf(UnsupportedCurrencyException.class)
        .hasMessage("Currency UAH is not supported.");
  }

  @ParameterizedTest
  @ValueSource(ints = {7, 8})
  void throwsExceptionIfExpiryDateIsNotInTheFuture(int month) {
    // given
    var request = validAuthorisePaymentRequest().toBuilder()
        .expiryDate(new ExpiryDate(ExpiryMonth.of(month), ExpiryYear.of(2026)))
        .build();

    // expect
    assertThatThrownBy(() -> underTest.create(request))
        .isInstanceOf(PastYearMonthException.class)
        .hasMessage("Month and year must be in the future.");
  }

}