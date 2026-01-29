package com.example.card_app.DTO.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;


@Data
@Builder
@Schema(description = "информация о карте")
@NoArgsConstructor
@AllArgsConstructor
public class CardDTO {

    @JsonProperty("id")
    @NotBlank
    @Schema(description = "id карты", example = "123")
    private UUID id;

    @JsonProperty("cardNumber")
    @NotBlank
    @Schema(description = "номер карты", example = "1234 5678 9012 3456")
    private String cardNumber;

    @JsonProperty("balance")
    @NotBlank
    @Schema(description = "баланс карты", example = "123")
    private BigDecimal balance;
}
