package com.checkout.payment.gateway.infrastructure.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    name = "bank-authorisation",
    url = "${clients.bank-authorisation.url}"
)
public interface BankAuthorisationClient {

  @PostMapping(path = "/payments", produces = MediaType.APPLICATION_JSON_VALUE)
  AuthorisePaymentHttpResponse authorise(@RequestBody AuthorisePaymentHttpRequest request);

}
