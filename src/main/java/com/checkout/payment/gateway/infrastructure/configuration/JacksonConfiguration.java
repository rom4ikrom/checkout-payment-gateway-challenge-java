package com.checkout.payment.gateway.infrastructure.configuration;

import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.cfg.EnumFeature;

@Configuration
public class JacksonConfiguration {

  @Bean
  public JsonMapperBuilderCustomizer jsonMapperCustomizer() {
    return builder -> {
      builder.enable(EnumFeature.WRITE_ENUMS_TO_LOWERCASE);
      builder.addModule(new TinyTypeModule());
    };
  }

}
