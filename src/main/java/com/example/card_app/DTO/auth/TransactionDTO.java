package com.example.card_app.DTO.auth;

import com.example.card_app.Const.TransactionStatus;
import com.example.card_app.Const.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@Schema(description = "Информация о транзакциях")
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {

    private UUID id;
    private String fromCard;
    private String toCard;
    private BigDecimal amount;
    private TransactionType type;
    private TransactionStatus status;
    private Instant createdAt;
}
