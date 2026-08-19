package com.checkout.payment.gateway.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.checkout.payment.gateway.domain.api.AuthorisationApi;
import com.checkout.payment.gateway.domain.exception.PastYearMonthException;
import com.checkout.payment.gateway.domain.exception.UnsupportedCurrencyException;
import com.checkout.payment.gateway.domain.model.authorisation.AuthorisePaymentRequest;
import com.checkout.payment.gateway.domain.model.payment.Payment;
import com.checkout.payment.gateway.domain.model.payment.PaymentStatus;
import com.checkout.payment.gateway.domain.model.values.CardCvv;
import com.checkout.payment.gateway.domain.model.values.CardNumber;
import com.checkout.payment.gateway.domain.model.values.CardNumberLastFour;
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
  private PaymentIdGenerator paymentIdGenerator;
  @Mock
  private AuthorisationApi authorisationApi;

  @BeforeEach
  void setup() {
    underTest = new PaymentService(paymentIdGenerator, authorisationApi, CLOCK);
  }

  @Test
  void returnsAuthorisedPayment() {
    // given
    var paymentId = PaymentId.of("ede16a47-8709-4b55-8dac-b69f084c4ef2");
    when(paymentIdGenerator.nextId()).thenReturn(paymentId);

    // when
    var result = underTest.create(validRequest());

    // then
    assertThat(result).isEqualTo(
        Payment.builder()
            .id(paymentId)
            .status(PaymentStatus.AUTHORIZED)
            .lastFourCardDigits(CardNumberLastFour.of("1234"))
            .expiryDate(new ExpiryDate(ExpiryMonth.of(8), ExpiryYear.of(2026)))
            .amount(Money.of(CurrencyUnit.GBP, new BigDecimal("42.01")))
            .build()
    );
  }

  @Test
  void throwsExceptionIfCurrencyIsNotSupported() {
    // given
    var request = validRequest().toBuilder()
        .currency(CurrencyUnit.of("UAH"))
        .build();

    // when and then
    assertThatThrownBy(() -> underTest.create(request))
        .isInstanceOf(UnsupportedCurrencyException.class)
        .hasMessage("Currency UAH is not supported.");
  }

  @ParameterizedTest
  @ValueSource(ints = {7, 8})
  void throwsExceptionIfExpiryDateIsNotInTheFuture(int month) {
    // given
    var request = validRequest().toBuilder()
        .expiryDate(new ExpiryDate(ExpiryMonth.of(month), ExpiryYear.of(2026)))
        .build();

    // when and then
    assertThatThrownBy(() -> underTest.create(request))
        .isInstanceOf(PastYearMonthException.class)
        .hasMessage("Month and year must be in the future.");
  }

  private static AuthorisePaymentRequest validRequest() {
    return AuthorisePaymentRequest.builder()
        .cardNumber(CardNumber.of("4444333322221111"))
        .expiryDate(new ExpiryDate(ExpiryMonth.of(9), ExpiryYear.of(2026)))
        .cardCvv(CardCvv.of("123"))
        .currency(CurrencyUnit.GBP)
        .amount(4201)
        .build();
  }

}