package com.checkout.payment.gateway.infrastructure.configuration;

import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfiguration {

  @Bean
  public JsonMapperBuilderCustomizer jsonMapperCustomizer() {
    return builder -> {
      builder.addModule(new PaymentGatewayModule());
    };
  }

}
