package com.example.card_app.Service;

import java.util.UUID;

public interface EncryptionService {

    UUID encrypt(UUID data);

    UUID decrypt(UUID encryptedData);
}
