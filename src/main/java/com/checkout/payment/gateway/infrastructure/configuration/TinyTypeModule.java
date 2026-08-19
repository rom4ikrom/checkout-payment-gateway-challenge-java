package com.checkout.payment.gateway.infrastructure.configuration;

import com.checkout.payment.gateway.domain.model.values.NonBlankString;
import com.checkout.payment.gateway.domain.model.values.PositiveInteger;
import org.joda.money.CurrencyUnit;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.module.SimpleModule;

public class TinyTypeModule extends SimpleModule {

  public TinyTypeModule() {
    super("TinyTypeModule");
    addSerializer(NonBlankString.class, new NonBlankStringSerializer());
    addSerializer(PositiveInteger.class, new PositiveIntegerSerializer());
  }

  public static class NonBlankStringSerializer extends ValueSerializer<NonBlankString> {
    @Override
    public void serialize(NonBlankString value, JsonGenerator gen, SerializationContext ctxt) throws JacksonException {
      gen.writeString(value.value());
    }
  }

  public static class PositiveIntegerSerializer extends ValueSerializer<PositiveInteger> {
    @Override
    public void serialize(PositiveInteger value, JsonGenerator gen, SerializationContext ctxt) throws JacksonException {
      gen.writeNumber(value.value());
    }
  }

}
