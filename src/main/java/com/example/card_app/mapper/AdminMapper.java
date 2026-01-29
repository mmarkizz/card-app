package com.example.card_app.mapper;

import com.example.card_app.DTO.auth.CardDTO;
import com.example.card_app.DTO.auth.UserDTO;
import com.example.card_app.Entity.Card;
import com.example.card_app.Entity.User;
import com.example.card_app.utils.CardMaskUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminMapper {

    public UserDTO toDto(User user) {
        List<CardDTO> cards = user.getCards().stream()
                .map(card -> new CardDTO(
                        card.getId(),
                        CardMaskUtil.mask(card.getCardNumber()),
                        card.getBalance()
                ))
                .toList();

        return UserDTO.builder()
                .id(user.getId())
                .gmail(user.getGmail())
                .role(user.getRole())
                .cards(cards)
                .build();
    }

    public User toEntity(UserDTO userDTO){
        User user = new User();

        user.setId(userDTO.getId());
        user.setGmail(userDTO.getGmail());
        user.setRole(userDTO.getRole());

        return user;
    }
}
