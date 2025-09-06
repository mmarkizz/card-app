package com.example.card_app.DTO.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequestDTO {

    @NotBlank(message = "username не должен быть пустым")
    @Size(min = 3, max = 50, message = "размер от 3 до 50 символов")
    private String username;

    @NotBlank(message = "password не должен быть пустым")
    @Size(min = 4, max = 30, message = "размер от 4 до 30 символов")
    private String password;
}
