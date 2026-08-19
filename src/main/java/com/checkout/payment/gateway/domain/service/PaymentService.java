package com.checkout.payment.gateway.domain.service;

import com.checkout.payment.gateway.domain.api.AuthorisationApi;
import com.checkout.payment.gateway.domain.model.authorisation.AuthorisePaymentCommand;
import com.checkout.payment.gateway.domain.model.authorisation.AuthorisePaymentResponse;
import com.checkout.payment.gateway.domain.model.payment.Payment;
import com.checkout.payment.gateway.domain.model.payment.PaymentStatus;
import com.checkout.payment.gateway.domain.model.values.CardNumberLastFour;
import com.checkout.payment.gateway.domain.model.values.ExpiryDate;
import com.checkout.payment.gateway.domain.model.values.ExpiryMonth;
import com.checkout.payment.gateway.domain.model.values.ExpiryYear;
import com.checkout.payment.gateway.domain.model.values.PaymentId;
import lombok.RequiredArgsConstructor;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import java.math.BigDecimal;

@RequiredArgsConstructor
public class PaymentService {

  private final PaymentIdGenerator paymentIdGenerator;
  private final AuthorisationApi authorisationApi;

  Payment create(AuthorisePaymentCommand command) {
    return Payment.builder()
        .id(paymentIdGenerator.nextId())
        .status(PaymentStatus.AUTHORIZED)
        .lastFourCardDigits(CardNumberLastFour.of("1234"))
        .expiryDate(new ExpiryDate(ExpiryMonth.of(8), ExpiryYear.of(2026)))
        .amount(Money.of(CurrencyUnit.GBP, new BigDecimal("42.01")))
        .build();
  }


}
