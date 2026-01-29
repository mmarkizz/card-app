package com.example.card_app.DTO.auth;

import com.example.card_app.Const.RoleType;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@Schema(description = "Информация об авторе")
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private UUID id;
    private String gmail;
    private String password;
    private RoleType role;
    private List<CardDTO> cards;

    public UserDTO(UUID id, String gmail, RoleType role, List<CardDTO> cardDTOs) {
    }
}
