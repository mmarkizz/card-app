package com.example.card_app.Service;

import com.example.card_app.Const.CardStatus;
import com.example.card_app.Const.RoleType;
import com.example.card_app.DTO.auth.CardDTO;
import com.example.card_app.DTO.auth.UserDTO;
import com.example.card_app.Entity.Card;
import com.example.card_app.Entity.User;
import com.example.card_app.Repository.CardRepository;
import com.example.card_app.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Secured("ROLE_ADMIN")
public class AdminCRUDService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDTO findUserById(UUID id) {
        User newUser =  userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));

        List<CardDTO> cardDTOs = newUser.getCards().stream()
                .map(card -> new CardDTO(card.getId(), card.getCardNumber(), card.getBalance())) // подставь поля из Card
                .toList();

        return new UserDTO(newUser.getId(), newUser.getGmail(), newUser.getRole(), cardDTOs);
    }

    public void createUser(@Valid UserDTO req){
        User user = new User();
        user.setGmail(req.getGmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRole(req.getRole());
        userRepository.save(user);
    }

    public User updateUser(UUID id, User updatedUser) {
        User currentUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        currentUser.setGmail(updatedUser.getGmail());

        if (updatedUser.getPassword() != null) {
            currentUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        return userRepository.save(currentUser);
    }


    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    public Page<Card> findAllCards(UUID currentUserId, UUID requestedId, String statusFilter, Pageable pageable) throws AccessDeniedException{

        boolean isAdmn = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if(isAdmn){
            if(statusFilter!=null && !statusFilter.isEmpty()){
                return cardRepository.findAllByUserIdAndStatus(requestedId, CardStatus.valueOf(statusFilter), pageable);
            }
            return cardRepository.findAllByUserId(requestedId, pageable);
        }

        if(!currentUserId.equals(requestedId)){
            throw new AccessDeniedException("Вы можете просматривать только свои карты");
        }

        if(statusFilter!=null && !statusFilter.isEmpty()){
            return cardRepository.findAllByUserIdAndStatus(currentUserId, CardStatus.valueOf(statusFilter), pageable);
        }

        return cardRepository.findAllByUserId(currentUserId, pageable);
    }
}
