package com.checkout.payment.gateway.infrastructure.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;

public record AuthorisePaymentHttpResponse(
    boolean authorized,
    @Nullable @JsonProperty("authorization_code") String authorisationCode
) {

}
