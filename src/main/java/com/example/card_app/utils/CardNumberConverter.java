package com.example.card_app.utils;

import com.example.card_app.Service.EncryptionService;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Converter
@Component
public class CardNumberConverter implements AttributeConverter<UUID, UUID> {

    private static EncryptionService encryptionService;

    public static void setEncryptionService(EncryptionService encryptionService) {
        CardNumberConverter.encryptionService = encryptionService;
    }

    @Override
    public UUID convertToDatabaseColumn(UUID uuid) {
        if(uuid==null){
            return null;
        }
        if(encryptionService==null){
            throw new IllegalStateException("Сервис не может произвести шифрование");
        }
        return encryptionService.encrypt(uuid);
    }

    @Override
    public UUID convertToEntityAttribute(UUID uuid) {
        if(uuid==null){
            return null;
        }
        if(encryptionService==null){
            throw new IllegalStateException("Сервис не может произвести шифрование");
        }
        return encryptionService.decrypt(uuid);
    }
}
