package com.example.card_app.utils;

import java.util.Random;
import java.util.UUID;

public class CardNumberGeneratorToLunh {

    public static final Random random = new Random();

    public static UUID generateCardNumber(String bin, int length){

        StringBuilder number = new StringBuilder(bin);

        while (number.length()< length-1){
            number.append(random.nextInt (10));
        }

        int checkDigit = calculateLuhnCheckDigit(number.toString());
        number.append(checkDigit);

        return UUID.fromString(number.toString());  //похоже на костыль

    }

    private static int calculateLuhnCheckDigit(String number){
        int sum = 0;
        boolean alternate = true;

        for(int i = number.length()-1; i>=0; i--){
            int n = Character.getNumericValue(number.charAt(i));
            if(alternate){
                n*=2;
                if(n>9) n-=9;
            }
            sum+=n;
            alternate = !alternate;
        }

        return (10-(sum%10))%10;
    }
}
