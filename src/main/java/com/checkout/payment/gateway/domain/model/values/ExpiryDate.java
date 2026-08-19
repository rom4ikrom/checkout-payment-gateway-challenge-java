package com.checkout.payment.gateway.domain.model.values;

import lombok.Value;
import java.time.Clock;
import java.time.YearMonth;

@Value
public class ExpiryDate {

  ExpiryMonth month;
  ExpiryYear year;

  public boolean inFuture(Clock clock) {
    return YearMonth.of(year.value(), month.value()).isAfter(YearMonth.now(clock));
  }

  public boolean notInFuture(Clock clock) {
    return !inFuture(clock);
  }

}
