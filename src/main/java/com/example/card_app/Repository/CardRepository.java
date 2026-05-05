package com.example.card_app.Repository;

import com.example.card_app.Const.CardStatus;
import com.example.card_app.Entity.Card;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID> {

    // БЕЗОПАСНЫЙ ПЕРЕВОД
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    Select c from Card c
    where c.id = :cardId and c.user.id = :userId
    """)
    Optional<Card> findByIdAndUserIdForUpdate(
            @Param("cardId") UUID cardId,
            @Param("userId") UUID userId
    );

    // методы для удаления
    boolean existsByIdAndUserId(UUID cardId, UUID userId);
    void deleteByIdAndUserId(UUID cardId, UUID userId);
    Optional<Card> findByIdAndUserId(UUID cardId, UUID userId);

    // ИСПРАВЛЕНО: изменено на cardNumber
    Optional<Card> findByCardNumber(String cardNumber);

    List<Card> findAllByUserId(UUID userId);

    // пагинация и фильтры
    Page<Card> findAllByUserId(UUID userId, Pageable pageable);
    Page<Card> findAllByUserIdAndStatus(UUID userId, CardStatus status, Pageable pageable);
}