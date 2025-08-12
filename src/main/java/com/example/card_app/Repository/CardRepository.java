package com.example.card_app.Repository;

import com.example.card_app.Entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID> {

    boolean existsByIdAndUserd(UUID cardId, UUID userId);
    void deleteByIdAndUserId(UUID cardId, UUID userId);

    Optional<Card> findByIdAndUserId(UUID cardNumber, UUID id);
    Card findCardByNumber(UUID cardNumber);
    List<Card> findAllCardsById(UUID id);
}
