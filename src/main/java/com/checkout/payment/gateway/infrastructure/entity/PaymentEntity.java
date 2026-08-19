package com.checkout.payment.gateway.infrastructure.entity;

import com.checkout.payment.gateway.domain.model.payment.CardDetails;
import com.checkout.payment.gateway.domain.model.payment.PaymentStatus;
import com.checkout.payment.gateway.domain.model.values.AuthorisationCode;
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
public class PaymentEntity {

  @NonNull
  PaymentId id;
  @NonNull
  CardDetails cardDetails;
  @NonNull
  PaymentStatus status;
  @NonNull
  Money amount;

  @Getter(AccessLevel.PRIVATE)
  @Nullable
  AuthorisationCode authorisationCode;

  public AuthorisationCode authorisationCodeOrNull() {
    return authorisationCode;
  }

}
