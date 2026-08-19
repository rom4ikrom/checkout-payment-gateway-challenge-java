package com.checkout.payment.gateway.domain.model.payment;

import com.checkout.payment.gateway.domain.model.values.CardCvv;
import com.checkout.payment.gateway.domain.model.values.CardNumber;
import com.checkout.payment.gateway.domain.model.values.CardNumberLastFour;
import com.checkout.payment.gateway.domain.model.values.ExpiryDate;
import lombok.NonNull;
import lombok.Value;

@Value
public class CardDetails {

  @NonNull
  CardNumber cardNumber;
  @NonNull
  ExpiryDate expiryDate;
  @NonNull
  CardCvv cardCvv;

  public CardNumberLastFour cardNumberLastFour() {
    return cardNumber.lastFour();
  }

}
