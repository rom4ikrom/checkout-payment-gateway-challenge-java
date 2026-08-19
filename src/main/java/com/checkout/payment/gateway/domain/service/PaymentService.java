package com.checkout.payment.gateway.domain.service;

import com.checkout.payment.gateway.domain.api.AuthorisationApi;
import com.checkout.payment.gateway.domain.exception.PastYearMonthException;
import com.checkout.payment.gateway.domain.exception.UnsupportedCurrencyException;
import com.checkout.payment.gateway.domain.model.authorisation.AuthorisePaymentRequest;
import com.checkout.payment.gateway.domain.model.payment.Payment;
import com.checkout.payment.gateway.domain.model.payment.PaymentStatus;
import com.checkout.payment.gateway.domain.model.values.CardNumberLastFour;
import com.checkout.payment.gateway.domain.model.values.ExpiryDate;
import com.checkout.payment.gateway.domain.model.values.ExpiryMonth;
import com.checkout.payment.gateway.domain.model.values.ExpiryYear;
import lombok.RequiredArgsConstructor;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import java.math.BigDecimal;
import java.time.Clock;
import java.util.Set;

@RequiredArgsConstructor
public class PaymentService {

  private static final Set<CurrencyUnit> ACCEPTABLE_CURRENCIES = Set.of(
      CurrencyUnit.GBP,
      CurrencyUnit.USD,
      CurrencyUnit.EUR
  );

  private final PaymentIdGenerator paymentIdGenerator;
  private final AuthorisationApi authorisationApi;
  private final Clock clock;

  Payment create(AuthorisePaymentRequest request) {
    checkIfSupported(request);

    return Payment.builder()
        .id(paymentIdGenerator.nextId())
        .status(PaymentStatus.AUTHORIZED)
        .lastFourCardDigits(CardNumberLastFour.of("1234"))
        .expiryDate(new ExpiryDate(ExpiryMonth.of(8), ExpiryYear.of(2026)))
        .amount(Money.of(CurrencyUnit.GBP, new BigDecimal("42.01")))
        .build();
  }

  private void checkIfSupported(AuthorisePaymentRequest request) {
    if (!ACCEPTABLE_CURRENCIES.contains(request.currency())) {
      throw new UnsupportedCurrencyException(request.currency());
    }

    if (request.expiryDate().notInFuture(clock)) {
      throw new PastYearMonthException();
    }
  }

}
