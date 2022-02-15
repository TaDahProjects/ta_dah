package com.tadah.vehicle.utils;

import com.google.protobuf.AbstractMessageLite;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.AbstractMessageConverter;
import org.springframework.util.MimeType;

import java.lang.reflect.InvocationTargetException;

public final class ProtobufMessageConverter extends AbstractMessageConverter {
    public ProtobufMessageConverter() {
        super(new MimeType("application", "x-protobuf"));
    }

    @Override
    protected boolean supports(final Class<?> clazz) {
        return AbstractMessageLite.class.isAssignableFrom(clazz);
    }

    @Override
    protected Object convertToInternal(final Object payload, final MessageHeaders headers, final Object conversionHint) {
        return ((AbstractMessageLite) payload).toByteArray();
    }

    @Override
    protected Object convertFromInternal(final Message<?> message, final Class<?> targetClass, final Object conversionHint) {
        final Object payload = message.getPayload();

        try {
            return targetClass.getMethod("parseFrom", byte[].class)
                .invoke(null, payload);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException exception) {
            throw new IllegalStateException("Unsupported input: " + payload, exception);
        }
    }
}
