package com.example.card_app.Repository;

import com.example.card_app.Const.CardStatus;
import com.example.card_app.Entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID> {

//методы для удаления
    boolean existsByIdAndUserId(UUID cardId, UUID userId);
    void deleteByIdAndUserId(UUID cardId, UUID userId);

    Optional<Card> findByIdAndUserId(UUID cardNumber, UUID id);
    Card findCardByNumber(UUID cardNumber);
    List<Card> findAllCardsByUserId(UUID id);

    //пагинация и фильтры
    Page<Card> findAllCardsByUserIdToPage(UUID userId, Pageable pageable);
    Page<Card> findAllByUserIdAndStatus (UUID userId, CardStatus status, Pageable pageable);
}
