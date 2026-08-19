package com.checkout.payment.gateway.domain.model.authorisation;

import com.checkout.payment.gateway.domain.model.values.CardCvv;
import com.checkout.payment.gateway.domain.model.values.CardNumber;
import com.checkout.payment.gateway.domain.model.values.ExpiryDate;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.joda.money.CurrencyUnit;

@Value
@Builder(toBuilder = true)
public class AuthorisePaymentRequest {

  @NonNull
  CardNumber cardNumber;
  @NonNull
  ExpiryDate expiryDate;
  @NonNull
  CardCvv cardCvv;

  @NonNull
  CurrencyUnit currency;
  int amount;

}
