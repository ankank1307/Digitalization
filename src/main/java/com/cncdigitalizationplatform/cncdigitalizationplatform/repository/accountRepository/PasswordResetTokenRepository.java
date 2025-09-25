package com.cncdigitalizationplatform.cncdigitalizationplatform.repository.accountRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.accountEntities.Account;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.accountEntities.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, String> {
    Optional<PasswordResetToken> findByToken(String token);
    Optional<PasswordResetToken> findByAccountAndUsedFalse(Account account);
    void deleteByAccount(Account account);
    void deleteByExpiryDateBefore(LocalDateTime date);
}
