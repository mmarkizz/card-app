package com.example.card_app.Service;

import java.util.UUID;

public interface EncryptionService {

    String encrypt(String data);

    String decrypt(String encryptedData);
}
