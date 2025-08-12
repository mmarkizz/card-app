package com.example.card_app.Service;

import com.example.card_app.Entity.User;
import com.example.card_app.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Secured("ROLE_ADMIN")
public class AdminCRUDService {

    @Autowired
    private UserRepository userRepository;

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


}
