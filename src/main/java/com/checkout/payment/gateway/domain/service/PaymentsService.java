package com.checkout.payment.gateway.domain.service;

import com.checkout.payment.gateway.domain.api.AuthorisationApi;
import com.checkout.payment.gateway.domain.exception.PastYearMonthException;
import com.checkout.payment.gateway.domain.exception.UnsupportedCurrencyException;
import com.checkout.payment.gateway.domain.model.authorisation.AuthorisePaymentRequest;
import com.checkout.payment.gateway.domain.model.authorisation.AuthorisePaymentResponse;
import com.checkout.payment.gateway.domain.model.payment.Payment;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.joda.money.CurrencyUnit;
import java.time.Clock;
import java.util.Set;

@RequiredArgsConstructor
public class PaymentsService {

  private static final Set<CurrencyUnit> ACCEPTABLE_CURRENCIES = Set.of(
      CurrencyUnit.GBP,
      CurrencyUnit.USD,
      CurrencyUnit.EUR
  );

  @NonNull
  private final Clock clock;
  @NonNull
  private final AuthorisationApi authorisationApi;
  @NonNull
  private final PaymentFactory paymentFactory;

  public Payment create(AuthorisePaymentRequest request) {
    checkIfSupported(request);
    AuthorisePaymentResponse response = authorisationApi.authorisePayment(request);
    return paymentFactory.create(request, response);
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
