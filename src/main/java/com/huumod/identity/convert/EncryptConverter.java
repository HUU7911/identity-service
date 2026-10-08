package com.huumod.identity.convert;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Objects;

@Component
@Converter
public class EncryptConverter implements AttributeConverter<String, String> {
    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (Objects.isNull(attribute)) {
            return null;
        }
        return Base64.getEncoder().encodeToString(attribute.getBytes());
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (Objects.isNull(dbData)) {
            return null;
        }
        return new String(Base64.getDecoder().decode(dbData));
    }
}
