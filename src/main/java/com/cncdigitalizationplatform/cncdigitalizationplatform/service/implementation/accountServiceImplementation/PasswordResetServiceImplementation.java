package com.cncdigitalizationplatform.cncdigitalizationplatform.service.implementation.accountServiceImplementation;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cncdigitalizationplatform.cncdigitalizationplatform.Exceptions.InvalidTokenException;
import com.cncdigitalizationplatform.cncdigitalizationplatform.Exceptions.ResourceNotFoundException;
import com.cncdigitalizationplatform.cncdigitalizationplatform.Exceptions.UnauthorizedException;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.JwtUtil;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.accountEntities.Account;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.accountEntities.PasswordResetToken;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.accountRepository.AccountRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.accountRepository.PasswordResetTokenRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.service.accountService.EmailService;

@Service
@Transactional
public class PasswordResetServiceImplementation {
    
    @Autowired
    private PasswordResetTokenRepository tokenRepository;
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    private static final SecureRandom random = new SecureRandom();
    
    public void initiatePasswordReset(String email) {
        // Find account by username
        Account account = accountRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("account"));
        
        // Generate reset token
        String resetToken = generateSecureToken();
        
        // Delete any existing unused tokens for this account
        tokenRepository.deleteByAccount(account);
        
        // Create new reset token
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setAccount(account);
        passwordResetToken.setToken(resetToken);
        tokenRepository.save(passwordResetToken);
        
        // Send email
        emailService.sendPasswordResetEmail(email, resetToken);
    }
    
    public void resetPassword(String token, String newPassword, String method, String confirmPassword, String currentPassword) {
        if ("update".equals(method)) {
            if (!jwtUtil.validateToken(token)) {
                throw new UnauthorizedException("token");
            }
            String tokenAccountId = jwtUtil.getAccountIdFromToken(token);
            Account account = accountRepository.findById(tokenAccountId)
                .orElseThrow(() -> new ResourceNotFoundException("account"));
            
            if (currentPassword == null) {
                throw new Error("passwordIncorrect");
            }
            
            if (!currentPassword.equals(account.getPassword())) {
                throw new Error("passwordIncorrect");
            }
            
            if (!newPassword.equals(confirmPassword)) {
                throw new Error("mismatch");
            }
            
            account.setPassword(newPassword);
            accountRepository.save(account);
            
        } else if ("reset".equals(method)) {
            // Find token
            PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("token"));
            
            // Validate token
            if (resetToken.isUsed()) {
                throw new InvalidTokenException("used");
            }
            
            if (resetToken.isExpired()) {
                throw new InvalidTokenException("expired");
            }
            
            // Update password
            Account account = resetToken.getAccount();
            account.setPassword(newPassword);
            accountRepository.save(account);
            
            // Mark token as used
            resetToken.setUsed(true);
            tokenRepository.save(resetToken);
        } else {
            throw new ResourceNotFoundException("method should be 'reset' or 'update'. method was: " + method);
        }
    }
    
    public boolean validateResetToken(String token) {
        Optional<PasswordResetToken> resetToken = tokenRepository.findByToken(token);
        
        if (resetToken.isEmpty()) {
            return false;
        }
        
        PasswordResetToken tokenEntity = resetToken.get();
        return !tokenEntity.isUsed() && !tokenEntity.isExpired();
    }
    
    private String generateSecureToken() {
        byte[] tokenBytes = new byte[32];
        random.nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }
    
    @Scheduled(cron = "0 0 2 * * ?") // Run daily at 2 AM
    public void cleanupExpiredTokens() {
        tokenRepository.deleteByExpiryDateBefore(LocalDateTime.now());
    }
}

