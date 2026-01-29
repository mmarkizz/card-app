package com.example.card_app.utils;

public final class CardMaskUtil {

    private CardMaskUtil() {}

    public static String mask(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        return "**** **** **** " +
                cardNumber.substring(cardNumber.length() - 4);
    }
}
