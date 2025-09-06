package com.example.card_app.DTO.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenResponseDTO {

    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";

    public TokenResponseDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
