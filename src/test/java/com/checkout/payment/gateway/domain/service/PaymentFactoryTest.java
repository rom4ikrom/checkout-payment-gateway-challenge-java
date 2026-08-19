package com.checkout.payment.gateway.domain.service;

import com.checkout.payment.gateway.domain.exception.RejectedAuthorisationException;
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
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.List;

import static com.checkout.payment.gateway.domain.utils.TestFixtures.validAuthorisePaymentRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentFactoryTest {

  @Mock
  private PaymentIdGenerator paymentIdGenerator;

  private PaymentFactory underTest;

  @BeforeEach
  void setup() {
    var authorisedPaymentStrategy = new AuthorisedPaymentStrategy(paymentIdGenerator);
    var declinedPaymentStrategy = new DeclinedPaymentStrategy(paymentIdGenerator);
    underTest = new PaymentFactory(List.of(authorisedPaymentStrategy, declinedPaymentStrategy));
  }

  @Test
  void createsAuthorisedPayment() {
    // given
    var request = validAuthorisePaymentRequest();
    var authorisationCode = "b31d145c-d080-403a-bd0c-e5243a970cf3";
    var response = new AuthorisePaymentResponse.Authorised(AuthorisationCode.of(authorisationCode));
    var paymentId = "d4ac2e71-75fa-45ce-9441-2bfb87e3e956";
    when(paymentIdGenerator.nextId()).thenReturn(PaymentId.of(paymentId));

    // when
    var payment = underTest.create(request, response);

    // then
    var expectedPayment = Payment.builder()
        .id(PaymentId.of(paymentId))
        .cardDetails(new CardDetails(
            CardNumber.of("4444333322221111"),
            new ExpiryDate(ExpiryMonth.of(9), ExpiryYear.of(2026)),
            CardCvv.of("123")))
        .status(PaymentStatus.AUTHORIZED)
        .amount(Money.of(CurrencyUnit.GBP, new BigDecimal("42.01")))
        .authorisationCode(AuthorisationCode.of(authorisationCode))
        .build();
    assertThat(payment).isEqualTo(expectedPayment);
  }

  @Test
  void createsDeclinedPayment() {
    // given
    var request = validAuthorisePaymentRequest();
    var response = new AuthorisePaymentResponse.Declined();
    var paymentId = "b8c464fe-7d2b-4c6e-90ee-27c15507a287";
    when(paymentIdGenerator.nextId()).thenReturn(PaymentId.of(paymentId));

    // when
    var payment = underTest.create(request, response);

    // then
    var expectedPayment = Payment.builder()
        .id(PaymentId.of(paymentId))
        .cardDetails(new CardDetails(
            CardNumber.of("4444333322221111"),
            new ExpiryDate(ExpiryMonth.of(9), ExpiryYear.of(2026)),
            CardCvv.of("123")))
        .status(PaymentStatus.DECLINED)
        .amount(Money.of(CurrencyUnit.GBP, new BigDecimal("42.01")))
        .build();
    assertThat(payment).isEqualTo(expectedPayment);
  }

  @Test
  void throwsExceptionIfRejectedAuthorisation() {
    // given
    var request = validAuthorisePaymentRequest();
    var response = new AuthorisePaymentResponse.Rejected();

    // expect
    assertThatThrownBy(() -> underTest.create(request, response))
        .isInstanceOf(RejectedAuthorisationException.class)
        .hasMessage("Failed to authorise payment, authorisation rejected. Please try again later or use different card.");
  }




}