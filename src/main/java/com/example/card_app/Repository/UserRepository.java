package com.example.card_app.Repository;

import com.example.card_app.Entity.Card;
import com.example.card_app.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    User findUserById(UUID id);
}
