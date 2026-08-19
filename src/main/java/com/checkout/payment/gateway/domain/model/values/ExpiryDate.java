package com.checkout.payment.gateway.domain.model.values;

import lombok.NonNull;
import lombok.Value;
import java.time.Clock;
import java.time.YearMonth;

@Value
public class ExpiryDate {

  @NonNull
  ExpiryMonth month;
  @NonNull
  ExpiryYear year;

  public boolean inFuture(Clock clock) {
    return asYearMonth().isAfter(YearMonth.now(clock));
  }

  public boolean notInFuture(Clock clock) {
    return !inFuture(clock);
  }

  public YearMonth asYearMonth() {
    return YearMonth.of(year.value(), month.value());
  }

}
