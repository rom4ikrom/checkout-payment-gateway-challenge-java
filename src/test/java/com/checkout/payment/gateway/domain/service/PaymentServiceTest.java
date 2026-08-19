package com.checkout.payment.gateway.domain.service;

import com.checkout.payment.gateway.domain.api.AuthorisationApi;
import com.checkout.payment.gateway.domain.model.authorisation.AuthorisePaymentCommand;
import com.checkout.payment.gateway.domain.model.payment.Payment;
import com.checkout.payment.gateway.domain.model.payment.PaymentStatus;
import com.checkout.payment.gateway.domain.model.values.CardNumberLastFour;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

  private PaymentService underTest;

  @Mock
  private PaymentIdGenerator paymentIdGenerator;
  @Mock
  private AuthorisationApi authorisationApi;

  @BeforeEach
  void setup() {
    underTest = new PaymentService(paymentIdGenerator, authorisationApi);
  }

  @Test
  void returnsAuthorisedPayment() {
    // given
    var paymentId = PaymentId.of("ede16a47-8709-4b55-8dac-b69f084c4ef2");
    when(paymentIdGenerator.nextId()).thenReturn(paymentId);

    // when
    var result = underTest.create(new AuthorisePaymentCommand());

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

}