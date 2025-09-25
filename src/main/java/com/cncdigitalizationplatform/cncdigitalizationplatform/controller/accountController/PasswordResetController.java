package com.cncdigitalizationplatform.cncdigitalizationplatform.controller.accountController;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cncdigitalizationplatform.cncdigitalizationplatform.Exceptions.InvalidTokenException;
import com.cncdigitalizationplatform.cncdigitalizationplatform.Exceptions.ResourceNotFoundException;
import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.accountDto.PasswordResetEmail;
import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.accountDto.ResetPasswordRequest;
import com.cncdigitalizationplatform.cncdigitalizationplatform.service.implementation.accountServiceImplementation.PasswordResetServiceImplementation;

@RestController
@RequestMapping("/api/password-reset")
public class PasswordResetController {
    
    @Autowired
    private PasswordResetServiceImplementation passwordResetService;
    
    @PostMapping("/initiate")
    public ResponseEntity<Map<String, String>> initiatePasswordReset(
            @RequestBody PasswordResetEmail request) {
        
        try {
            passwordResetService.initiatePasswordReset(request.getEmail());
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "sent");
            return ResponseEntity.ok(response);
            
        } catch (ResourceNotFoundException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    @PostMapping("/reset")
    public ResponseEntity<Map<String, String>> resetPassword(
            @RequestBody ResetPasswordRequest request) {
        try {
            passwordResetService.resetPassword(request.getToken(), request.getNewPassword(), request.getMethod(), request.getConfirmPassword(), request.getCurrentPassword());
            Map<String, String> response = new HashMap<>();
            response.put("message", "Password reset successfully");
            return ResponseEntity.ok(response);
            
        } catch (InvalidTokenException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @GetMapping("/validate/{token}")
    public ResponseEntity<Map<String, Boolean>> validateToken(@PathVariable String token) {
        boolean isValid = passwordResetService.validateResetToken(token);
        
        Map<String, Boolean> response = new HashMap<>();
        response.put("valid", isValid);
        return ResponseEntity.ok(response);
    }
}
