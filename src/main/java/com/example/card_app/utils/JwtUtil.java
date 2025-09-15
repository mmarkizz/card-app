package com.example.card_app.utils;//генерация токенов

import com.example.card_app.Entity.User;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private String jwtSecret = "qwertyuiopasdfghjklzxcvbnm";

    private long accessExpiration;
    private long refreshExpiration;

    public String generateAccessToken(User user){

        return Jwts.builder()
                .setSubject(user.getGmail())
                .claim("roles", user.getRoles().stream().map(Enum::name).toList())
                .setIssuedAt(new Date())
                .setExpiration(new Date().getTime()+ accessExpiration)
                .


    }
}
