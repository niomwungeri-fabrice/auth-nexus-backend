package io.lynx.controllers;

import io.lynx.dto.CreateAccountRequest;
import io.lynx.dto.GenericAPIResponse;
import io.lynx.dto.TOTPGenerateRequest;
import io.lynx.models.Account;
import io.lynx.service.AccountService;
import io.lynx.utils.TotpUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@Slf4j
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }


    @PostMapping
    public ResponseEntity<?> addAccount(HttpServletRequest request, @RequestBody CreateAccountRequest createAccountRequest) {
        String email = (String) request.getAttribute("email");
        Account savedAccount = accountService.saveAccount(email, createAccountRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new GenericAPIResponse("account created successfully", savedAccount, HttpStatus.CREATED.value()));
    }

    @GetMapping
    public ResponseEntity<?> getAccounts(HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        return ResponseEntity.ok(new GenericAPIResponse("accounts retrieved successfully", accountService.getAccountsByEmail(email), HttpStatus.OK.value()));
    }

    @PostMapping("/totp")
    public ResponseEntity<?> getTotp(HttpServletRequest request, @RequestBody TOTPGenerateRequest totpGenerateRequest) {
        String email = (String) request.getAttribute("email"); // Extracted from JWT
        Account account = accountService.getAccountByEmailAndName(email, totpGenerateRequest.getAccountName());

        // Generate TOTP for the account
        String totp = TotpUtil.generateTotp(account.getSecretKey());
        return ResponseEntity.ok(new GenericAPIResponse("TOTP generated successfully", totp, HttpStatus.OK.value()));
    }
}
