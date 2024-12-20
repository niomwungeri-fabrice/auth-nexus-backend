package io.lynx.service;

import io.lynx.dto.CreateAccountRequest;
import io.lynx.exceptions.InternalServerErrorException;
import io.lynx.exceptions.ResourceNotFoundException;
import io.lynx.models.Account;
import io.lynx.models.User;
import io.lynx.repository.AccountRepository;
import io.lynx.repository.UserRepository;
import io.lynx.utils.EncryptionUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponseException;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final EncryptionUtil encryptionUtil;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository, EncryptionUtil encryptionUtil) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.encryptionUtil = encryptionUtil;
    }

    public Account saveAccount(String email, CreateAccountRequest accountRequest) {
        try {

            // validate email

            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isEmpty()) {
                throw new ResourceNotFoundException("user not found with email: " + email);
            }

            Optional<Account> existingAccount = accountRepository.findByAccountName(accountRequest.getAccountName());
            if (existingAccount.isPresent()) {
                // Return the existing account if it already exists
                return existingAccount.get();
            }

            // Encrypt the secret key before saving
            String encryptedSecretKey = encryptionUtil.encrypt(accountRequest.getSecretKey());

            User user = userOptional.get();
            Account account = Account.builder()
                    .accountName(accountRequest.getAccountName())
                    .secretKey(encryptedSecretKey)
                    .user(user)
                    .build();
            return accountRepository.save(account);
        } catch (InternalServerErrorException e) {
            log.error("error saving account: {}", e.getMessage());
            throw new InternalServerErrorException("error saving account: " + e.getMessage());
        }
    }

    public Account getAccountByEmailAndName(String email, String accountName) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        return accountRepository.findAccountByAccountNameAndUser(accountName, user)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found for name: " + accountName));
    }

    public List<Account> getAccountsByEmail(String email) {
        return accountRepository.findByUserEmail(email);
    }

}