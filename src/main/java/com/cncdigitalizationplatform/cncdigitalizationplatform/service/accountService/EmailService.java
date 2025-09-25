package com.cncdigitalizationplatform.cncdigitalizationplatform.service.accountService;

public interface EmailService {
    void sendPasswordResetEmail(String toEmail, String resetToken);
    
    String buildPasswordResetEmailTemplate(String resetUrl);
}
