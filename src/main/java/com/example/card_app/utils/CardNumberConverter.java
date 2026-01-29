package com.example.card_app.utils;

import com.example.card_app.Service.EncryptionService;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Converter
@RequiredArgsConstructor
public class CardNumberConverter implements AttributeConverter<String, String> {

    private final EncryptionService encryptionService;

    @Override
    public String convertToDatabaseColumn(String cardNumber) {
        if (encryptionService == null) {
            throw new IllegalStateException("EncryptionService not injected");
        }
        return encryptionService.encrypt(cardNumber);
    }

    @Override
    public String convertToEntityAttribute(String encrypted) {
        if (encrypted == null) return null;
        return encryptionService.decrypt(encrypted);
    }
}


