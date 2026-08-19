package com.checkout.payment.gateway.domain.model.values;

import lombok.Value;

@Value
public class ExpiryDate {

  ExpiryMonth month;
  ExpiryYear year;

}
