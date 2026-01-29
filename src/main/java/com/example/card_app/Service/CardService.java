package com.example.card_app.Service;

import com.example.card_app.Const.CardStatus;
import com.example.card_app.Const.RoleType;
import com.example.card_app.Const.TransactionStatus;
import com.example.card_app.Const.TransactionType;
import com.example.card_app.Entity.Card;
import com.example.card_app.Entity.Transaction;
import com.example.card_app.Entity.User;
import com.example.card_app.Repository.CardRepository;
import com.example.card_app.Repository.TransactionRepository;
import com.example.card_app.Repository.UserRepository;
import com.example.card_app.utils.CardNumberGeneratorToLunh;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardService {

    @Autowired
    private CardRepository cardRepository;
    private UserRepository userRepository;
    private EncryptionService encryptionService;
    @Autowired
    private TransactionRepository transactionRepository;

    public CardService(UserRepository userRepository, EncryptionService encryptionService, CardRepository cardRepository) {
        this.userRepository = userRepository;
        this.encryptionService = encryptionService;
        this.cardRepository=cardRepository;
    }


    public BigDecimal getBalanceById(UUID id){
        Card card = cardRepository.findById(id).orElseThrow(()->new RuntimeException("карта не найдена"));
        return card.getBalance();
    }

    public BigDecimal getFullBalance(UUID uuid){
        User currentUser = userRepository.findById(uuid).orElseThrow(()->new EntityNotFoundException("User not found"));
        List<Card> userCards = cardRepository.findAllCardsByUserId(uuid);

        return userCards.stream()
                .map(Card::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Card changeCardStatus(CardStatus newCardStatus,UUID id, UUID uuid) throws AccessDeniedException {
        User currentUser = userRepository.findById(uuid).orElseThrow(()->new EntityNotFoundException("User not found"));

        Card currentCard = cardRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Card not found"));

        if(currentUser.getRoles().equals(RoleType.USER)){
            if(currentCard.getUser().getId().equals(uuid)){
                if(newCardStatus.equals(CardStatus.BLOCKED)){
                    currentCard.setStatus(newCardStatus);
                } else {
                    throw new AccessDeniedException("Вы можете только заблокировать карту");
                }
            } else{
                throw new AccessDeniedException("Вы можете менять статус только своей карты");
            }
        }else{
            currentCard.setStatus(newCardStatus);
        }
        return cardRepository.save(currentCard);
    }

    @Transactional
    public void transfer(UUID userId, UUID fromCardId, UUID toCardId, BigDecimal amount) throws AccessDeniedException {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        Card from = cardRepository.findByIdAndUserId(fromCardId, userId)
                .orElseThrow(() -> new AccessDeniedException("Source card not found"));

        Card to = cardRepository.findById(toCardId)
                .orElseThrow(() -> new EntityNotFoundException("Target card not found"));

        if (from.getStatus() != CardStatus.ACTIVE || to.getStatus() != CardStatus.ACTIVE) {
            throw new IllegalStateException("Card is not active");
        }

        if (from.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient funds");
        }

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));
    }


    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void transferMoney(
            UUID userId,
            UUID fromCardId,
            UUID toCardId,
            BigDecimal amount) {

        Transaction tx = new Transaction();

        User currentUser = userRepository.findById(userId).orElseThrow(()->new EntityNotFoundException("User not found"));
        tx.setUser(currentUser);

        Card fromCard = cardRepository.findById(fromCardId).orElseThrow(()->new EntityNotFoundException("Card not found"));
        tx.setFromCard(fromCard);

        Card toCard = cardRepository.findById(toCardId).orElseThrow(()->new EntityNotFoundException("Card not found"));
        tx.setToCard(toCard);

        tx.setAmount(amount);
        tx.setType(TransactionType.TRANSFER);
        tx.setCreateAt(Instant.now());

        try {
            fromCard.setBalance(fromCard.getBalance().subtract(amount));
            toCard.setBalance(toCard.getBalance().add(amount));

            tx.setStatus(TransactionStatus.SUCCESS);
        } catch (Exception ex){
            tx.setStatus(TransactionStatus.FAILED);
            throw ex;
        }finally {
            transactionRepository.save(tx);
        }

    }


    @Transactional
    public void deleteCard(UUID currentCardId, UUID currentUserId)throws AccessDeniedException{
        boolean exists = cardRepository.existsByIdAndUserId(currentCardId, currentUserId);
        if(!exists){
            throw new AccessDeniedException("You can delete only your own cards");
        }
        cardRepository.deleteByIdAndUserId(currentCardId, currentUserId);
    }

    public Card findCardByNumber(UUID cardNumber, UUID currentid){

        User currentUser = userRepository.findById(currentid).orElseThrow(()->new EntityNotFoundException("User not found"));
        Card currentCard = cardRepository.findByIdAndUserId(cardNumber, currentUser.getId()).orElseThrow(()->new EntityNotFoundException("Card not found!"));

        if (currentCard == null && currentUser.getRoles().equals(RoleType.ADMIN)) {
            currentCard = cardRepository.findCardByNumber(cardNumber);
        }
        return currentCard;
    }
}
