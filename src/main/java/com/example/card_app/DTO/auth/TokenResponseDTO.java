package com.example.card_app.DTO.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TokenResponseDTO {

    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
}
