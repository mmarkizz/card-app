package com.example.card_app.Repository;

import com.example.card_app.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    User findUserById(UUID id);

    boolean existsByGmail(String gmail);
    Optional<User> findByGmail(String gmail);

}
