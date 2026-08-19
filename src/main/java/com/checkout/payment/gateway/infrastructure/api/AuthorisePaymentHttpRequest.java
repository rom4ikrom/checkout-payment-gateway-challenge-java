package com.checkout.payment.gateway.infrastructure.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record AuthorisePaymentHttpRequest(
    @NonNull @JsonProperty("card_number") String cardNumber,
    @NonNull @JsonProperty("expiry_date") String expiryDate,
    @NonNull String currency,
    @NonNull Integer amount,
    @NonNull String cvv
) {

}
