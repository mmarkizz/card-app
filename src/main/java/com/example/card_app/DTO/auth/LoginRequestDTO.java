package com.example.card_app.DTO.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {

    @NotBlank(message = "введите username")
    private String username;

    @NotBlank(message = "введите password")
    private String password;
}
