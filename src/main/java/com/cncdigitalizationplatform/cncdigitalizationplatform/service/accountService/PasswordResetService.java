package com.cncdigitalizationplatform.cncdigitalizationplatform.service.accountService;

public interface PasswordResetService {
    void initiatePasswordReset(String email);
    void resetPassword(String token, String newPassword);
    boolean validateResetToken(String token);
    String generateSecureToken();
    void cleanupExpiredTokens();
}
