package com.example.card_app.Controller;

import com.example.card_app.DTO.auth.TransactionDTO;
import com.example.card_app.DTO.auth.UserDTO;
import com.example.card_app.Entity.User;
import com.example.card_app.Repository.TransactionRepository;
import com.example.card_app.Service.AdminCRUDService;
import com.example.card_app.mapper.TransactionMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@Secured("ROLE_ADMIN")
@Validated
@Tag(name="Администраторы", description = "операции над администраторами")
public class AdminController {

    private final AdminCRUDService adminCRUDService;
    private final TransactionRepository transactionRepository;

    @Operation(summary = "Вывод пользователся по Id", description = "Позволяет выводить пользователя с указанным Id")
    @GetMapping("/find_user/{uuid}")
    public ResponseEntity<UserDTO> findUser(@PathVariable @Min(0) UUID uuid) {
        return ResponseEntity.ok(adminCRUDService.findUserById(uuid));
    }

    @Operation(summary = "Создать пользователя", description = "Позволяет создать нового пользователя с заданными параметрами")
    @PostMapping("/create_user")
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody @Valid UserDTO user) {
        adminCRUDService.createUser(user);
    }

    @GetMapping("/transactions")
    public Page<TransactionDTO> getTransactions(
            @AuthenticationPrincipal UUID userId,
            Pageable pageable) {


        return transactionRepository
                .findAllByUserId(userId, pageable)
                .map(TransactionMapper:: toTransactionDTO);
    }

}
