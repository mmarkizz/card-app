package com.example.card_app.Entity;

import com.example.card_app.Const.TransactionStatus;
import com.example.card_app.Const.TransactionType;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@EqualsAndHashCode
@Table(name="transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(optional=false)
    private Card fromCard;

    @ManyToOne(optional=false)
    private Card toCard;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @ManyToOne(optional = false)
    private User user;

    @Column(nullable = false)
    private Instant createAt;

    public Instant getCreatedAt() {
        return createAt;
    }
}
