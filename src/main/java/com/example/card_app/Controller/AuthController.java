package com.example.card_app.Controller;

import com.example.card_app.DTO.auth.LoginRequestDTO;
import com.example.card_app.DTO.auth.RefreshRequest;
import com.example.card_app.DTO.auth.RegisterRequestDTO;
import com.example.card_app.DTO.auth.TokenResponseDTO;
import com.example.card_app.Service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequestDTO req){
        authService.register(req);
        return ResponseEntity.ok(Map.of("message", "registered"));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody @Valid LoginRequestDTO req){
        return ResponseEntity.ok(authService.login(req));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDTO> refresh(@RequestBody RefreshRequest ref){
        return ResponseEntity.ok(authService.refresh(ref));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshRequest ref){
        authService.logout(ref.getRefreshToken());
        return ResponseEntity.ok(Map.of("message", "logged out"));
    }
}
