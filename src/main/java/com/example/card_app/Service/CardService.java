package com.example.card_app.Service;

import com.example.card_app.Const.CardStatus;
import com.example.card_app.Const.RoleType;
import com.example.card_app.Entity.Card;
import com.example.card_app.Entity.User;
import com.example.card_app.Repository.CardRepository;
import com.example.card_app.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardService {

    @Autowired
    private CardRepository cardRepository;
    private UserRepository userRepository;

    public BigDecimal getBalanceById(UUID id){
        Card card = cardRepository.findById(id).orElseThrow(()->new RuntimeException("карта не найдена"));
        return card.getBalance();
    }

    public BigDecimal getFullBalance(UUID uuid){
        User currentUser = userRepository.findById(uuid).orElseThrow(()->new EntityNotFoundException("User not found"));
        List<Card> userCards = cardRepository.findAllCardsById(uuid);

        return userCards.stream()
                .map(Card::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Card changeCardStatus(CardStatus newCardStatus, @RequestParam UUID id, UUID uuid) throws AccessDeniedException {
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

    public Card saveCard(Card card, UUID currentUserId) throws AccessDeniedException{
        User currentUser = userRepository.findById(currentUserId).orElseThrow(()->new EntityNotFoundException("User not found!"));
        if(card.getId()!= null){
            Card existngCard = cardRepository.findById(card.getId()).orElseThrow(()-> new EntityNotFoundException("Card not found!"));

            if(currentUser.getRoles().equals(RoleType.USER) && !existngCard.getUser().getId().equals(currentUserId)){
                throw new AccessDeniedException("We don`t change others cards");
            }
        } else{
            card.setUser(currentUser);
            card.setStatus(CardStatus.ACTIVE);
            if(card.getBalance()==null){
                card.setBalance(BigDecimal.ZERO);
            }
        }
        return cardRepository.save(card);
    }

    public void translationMoney(UUID id, UUID cardNumber1, UUID cardNumber2, BigDecimal amount) throws EntityNotFoundException{
        User currentUser = userRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("User not found"));

        Card cardFrom = cardRepository.findByIdAndUserId(cardNumber1, id).orElseThrow(()-> new EntityNotFoundException("Source card not found"));

        Card cardTo = cardRepository.findByIdAndUserId(cardNumber2, id).orElseThrow(()-> new EntityNotFoundException("Source card not found"));

        if(cardFrom.getId().equals(cardTo.getId())){
            throw new IllegalStateException("Нельзя переводить на ту же карту");
        }

        if(cardFrom.getBalance().compareTo(amount)<0 || amount.compareTo(BigDecimal.ZERO)<=0){
            throw new IllegalStateException("Ошибка перевода (Проверьте сумму перевода или баланс карты)");
        }

        cardFrom.setBalance(cardFrom.getBalance().subtract(amount));
        cardTo.setBalance(cardTo.getBalance().add(amount));
    }

    @Transactional
    public void deleteCard(UUID currentCardId, UUID currentUserId)throws AccessDeniedException{

        boolean exists = cardRepository.existsByIdAndUserd(currentCardId, currentUserId);

        if(!exists){
            throw new AccessDeniedException("You can delete only your own cards");
        }
        cardRepository.deleteByIdAndUserId(currentCardId, currentUserId);
    }

    public List<Card> findAllCards(UUID currentuuid, UUID requestedId) throws AccessDeniedException {

        User currentUser = userRepository.findById(currentuuid).orElseThrow(()->new EntityNotFoundException("User not found"));

        if(currentUser.getRoles().equals(RoleType.ADMIN)){
            return cardRepository.findAllCardsById(requestedId);
        }

        if(!currentuuid.equals(requestedId)){
            throw new AccessDeniedException("Вы можете просматривать только свои карты");
        }

        return cardRepository.findAllCardsById(currentuuid);
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
