package com.checkout.payment.gateway.infrastructure.api;

import com.checkout.payment.gateway.domain.api.AuthorisationApi;
import com.checkout.payment.gateway.domain.model.authorisation.AuthorisePaymentRequest;
import com.checkout.payment.gateway.domain.model.authorisation.AuthorisePaymentResponse;
import com.checkout.payment.gateway.domain.model.authorisation.AuthorisePaymentResponse.Authorised;
import com.checkout.payment.gateway.domain.model.authorisation.AuthorisePaymentResponse.Declined;
import com.checkout.payment.gateway.domain.model.authorisation.AuthorisePaymentResponse.Rejected;
import com.checkout.payment.gateway.domain.model.values.AuthorisationCode;
import com.checkout.payment.gateway.domain.model.values.ExpiryDate;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class BankAuthorisationApi implements AuthorisationApi {

  private static final DateTimeFormatter EXPIRY_DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/yyyy");

  @NonNull
  private final BankAuthorisationClient bankAuthorisationClient;

  @Override
  public AuthorisePaymentResponse authorisePayment(AuthorisePaymentRequest request) {
    try {
      AuthorisePaymentHttpRequest httpRequest = buildRequest(request);
      AuthorisePaymentHttpResponse httpResponse = bankAuthorisationClient.authorise(httpRequest);

      if (httpResponse.authorized()) {
        return new Authorised(AuthorisationCode.of(httpResponse.authorisationCode()));
      } else {
        return Declined.instance();
      }

    } catch (Exception ex) {
      return Rejected.instance();
    }
  }

  private AuthorisePaymentHttpRequest buildRequest(AuthorisePaymentRequest request) {
    ExpiryDate expiryDate = request.expiryDate();
    String formattedExpiryDate = expiryDate.asYearMonth().format(EXPIRY_DATE_FORMATTER);
    return AuthorisePaymentHttpRequest.builder()
        .cardNumber(request.cardNumber().value())
        .expiryDate(formattedExpiryDate)
        .currency(request.currency().getCode())
        .cvv(request.cardCvv().value())
        .amount(request.amount())
        .build();
  }
}
