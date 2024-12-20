package io.lynx.repository;

import io.lynx.models.Account;
import io.lynx.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountName(String accountName);
    List<Account> findByUserEmail(String email);
    Optional<Account> findAccountByAccountNameAndUser(String accountName, User user);
}