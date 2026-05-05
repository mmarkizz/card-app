package com.example.card_app.Service;

import com.example.card_app.Const.RoleType;
import com.example.card_app.DTO.auth.LoginRequestDTO;
import com.example.card_app.DTO.auth.RefreshRequest;
import com.example.card_app.DTO.auth.RegisterRequestDTO;
import com.example.card_app.DTO.auth.TokenResponseDTO;
import com.example.card_app.Entity.RefreshToken;
import com.example.card_app.Entity.User;
import com.example.card_app.Repository.RefreshTokenRepository;
import com.example.card_app.Repository.UserRepository;
import com.example.card_app.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public void register(RegisterRequestDTO req) {
        if(userRepository.existsByGmail(req.getUsername())) {
            throw new IllegalArgumentException("User is already registered");
        }

        User newUser = new User();
        newUser.setGmail(req.getUsername());
        newUser.setPassword(passwordEncoder.encode(req.getPassword()));
        newUser.setRole(RoleType.USER);
        userRepository.save(newUser);
    }

    @Transactional
    public TokenResponseDTO login(LoginRequestDTO log) {
        Authentication auth = authenticationManager.authenticate
                (new UsernamePasswordAuthenticationToken
                        (log.getUsername(), log.getPassword()));

        User user = userRepository.findByGmail(log.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        //сохранения токена в БД
        RefreshToken r = new RefreshToken();
        r.setToken(refreshToken);
        r.setUser(user);
        r.setExpiryDate(Instant.now().plusMillis(jwtUtil.getRefreshExpirationMs()));
        refreshTokenRepository.save(r);

        //возвращение токенов юзеру
        TokenResponseDTO tokens = new TokenResponseDTO();
        tokens.setAccessToken(accessToken);
        tokens.setRefreshToken(refreshToken);
        return tokens;
    }

    public void logout(String refreshToken) {
        refreshTokenRepository.findByToken(refreshToken).ifPresent(refreshTokenRepository::delete);
    }

    //для обновления токенов
    @Transactional
    public TokenResponseDTO refresh(RefreshRequest ref) {

        String token = ref.getRefreshToken();
        if(!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        String username = jwtUtil.getUsernameByToken(token);
        // ИСПРАВЛЕНО: вместо gmail используем переменную username
        User user = userRepository.findByGmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        RefreshToken stored = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new BadCredentialsException("Token not found"));

        if(stored.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(stored);
            throw new BadCredentialsException("Token expired");
        }

        refreshTokenRepository.delete(stored);
        String newr = jwtUtil.generateRefreshToken(user);
        RefreshToken newRefresh = new RefreshToken();
        newRefresh.setToken(newr);
        newRefresh.setUser(user);
        newRefresh.setExpiryDate(Instant.now().plusMillis(jwtUtil.getRefreshExpirationMs()));
        refreshTokenRepository.save(newRefresh);

        String newAccess = jwtUtil.generateAccessToken(user);
        TokenResponseDTO newTokens = new TokenResponseDTO();
        newTokens.setAccessToken(newAccess);
        newTokens.setRefreshToken(newRefresh.getToken());
        return newTokens;
    }


}