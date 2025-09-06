package com.example.card_app.Service;

import com.example.card_app.Const.CardStatus;
import com.example.card_app.Entity.Card;
import com.example.card_app.Entity.User;
import com.example.card_app.Repository.CardRepository;
import com.example.card_app.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Secured("ROLE_ADMIN")
public class AdminCRUDService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CardRepository cardRepository;

    public User findUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public User createUser(User user){
        return userRepository.save(user);
    }

    public User updateUser(UUID id, User updatedUser){
        User currentUser = userRepository.findUserById(id);

        currentUser.setGmail(updatedUser.getGmail());
        currentUser.setPassword(updatedUser.getPassword());
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
            return cardRepository.findAllCardsByUserIdToPage(requestedId, pageable);
        }

        if(!currentUserId.equals(requestedId)){
            throw new AccessDeniedException("Вы можете просматривать только свои карты");
        }

        if(statusFilter!=null && !statusFilter.isEmpty()){
            return cardRepository.findAllByUserIdAndStatus(currentUserId, CardStatus.valueOf(statusFilter), pageable);
        }

        return cardRepository.findAllCardsByUserIdToPage(currentUserId, pageable);
    }
}
