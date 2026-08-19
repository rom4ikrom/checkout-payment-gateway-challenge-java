package com.checkout.payment.gateway.domain.model.payment;

import com.checkout.payment.gateway.domain.model.values.AuthorisationCode;
import com.checkout.payment.gateway.domain.model.values.CardNumberLastFour;
import com.checkout.payment.gateway.domain.model.values.ExpiryDate;
import com.checkout.payment.gateway.domain.model.values.PaymentId;
import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Value;
import org.joda.money.Money;

@Value
@Builder
public class Payment {

  @NonNull
  PaymentId id;
  @NonNull
  PaymentStatus status;
  @NonNull
  CardNumberLastFour lastFourCardDigits;
  @NonNull
  ExpiryDate expiryDate;
  @NonNull
  Money amount;

  @Getter(AccessLevel.PRIVATE)
  @Nullable
  AuthorisationCode authorisationCode;

}
