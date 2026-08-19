package com.checkout.payment.gateway.domain.utils;

import com.checkout.payment.gateway.domain.model.authorisation.AuthorisePaymentRequest;
import com.checkout.payment.gateway.domain.model.values.CardCvv;
import com.checkout.payment.gateway.domain.model.values.CardNumber;
import com.checkout.payment.gateway.domain.model.values.ExpiryDate;
import com.checkout.payment.gateway.domain.model.values.ExpiryMonth;
import com.checkout.payment.gateway.domain.model.values.ExpiryYear;
import org.joda.money.CurrencyUnit;

public class TestFixtures {

  private TestFixtures() {}

  public static AuthorisePaymentRequest validAuthorisePaymentRequest() {
    return AuthorisePaymentRequest.builder()
        .cardNumber(CardNumber.of("4444333322221111"))
        .expiryDate(new ExpiryDate(ExpiryMonth.of(9), ExpiryYear.of(2026)))
        .cardCvv(CardCvv.of("123"))
        .currency(CurrencyUnit.GBP)
        .amount(4201)
        .build();
  }

}
