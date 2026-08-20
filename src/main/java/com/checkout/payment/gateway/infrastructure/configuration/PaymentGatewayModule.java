package com.checkout.payment.gateway.infrastructure.configuration;

import com.checkout.payment.gateway.domain.model.payment.PaymentStatus;
import com.checkout.payment.gateway.domain.model.values.NonBlankString;
import com.checkout.payment.gateway.domain.model.values.PositiveInteger;
import org.apache.commons.lang3.StringUtils;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.module.SimpleModule;
import java.util.Locale;

public class PaymentGatewayModule extends SimpleModule {

  public PaymentGatewayModule() {
    super("PaymentGatewayModule");
    addSerializer(NonBlankString.class, new NonBlankStringSerializer());
    addSerializer(PositiveInteger.class, new PositiveIntegerSerializer());
    addSerializer(PaymentStatus.class, new PaymentStatusSerializer());
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

  public static class PaymentStatusSerializer extends ValueSerializer<PaymentStatus> {
    @Override
    public void serialize(PaymentStatus value, JsonGenerator gen, SerializationContext ctxt)
        throws JacksonException {
      gen.writeString(StringUtils.capitalize(value.name().toLowerCase(Locale.ROOT)));
    }
  }

}
