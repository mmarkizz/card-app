package com.example.card_app.mapper;

import com.example.card_app.DTO.auth.TransactionDTO;
import com.example.card_app.Entity.Transaction;
import com.example.card_app.utils.CardMaskUtil;

public class TransactionMapper {

    public static TransactionDTO toTransactionDTO(Transaction tx) {

        return new TransactionDTO(
                tx.getId(),
                CardMaskUtil.mask(tx.getToCard().getCardNumber()),
                CardMaskUtil.mask(tx.getFromCard().getCardNumber()),
                tx.getAmount(),
                tx.getType(),
                tx.getStatus(),
                tx.getCreatedAt()
        );

    }
}
