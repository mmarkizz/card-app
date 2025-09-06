package com.example.card_app.Repository;

import com.example.card_app.Entity.RefreshToken;
import com.example.card_app.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByToken(String token);
    void deleteAllByUser(User user);

}
