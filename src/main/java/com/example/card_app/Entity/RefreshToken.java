package com.example.card_app.Entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {
    @jakarta.persistence.Id
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private Instant expiryDate;

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public User getUser(){
        return user;
    }
    public void setUser(User user){}

    public void setToken(String token){
        this.token = token;
    }

    public String getToken(){
        return token;
    }

    public void setExpiryDate(Instant expiryDate){
        this.expiryDate = expiryDate;
    }
    public Instant getExpiryDate(){
        return expiryDate;
    }
}
