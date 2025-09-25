package com.cncdigitalizationplatform.cncdigitalizationplatform.controller.accountController;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.accountDto.AccountDto;
import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.accountDto.LoginRequestDto;
import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.accountDto.LoginResponseDto;
import com.cncdigitalizationplatform.cncdigitalizationplatform.service.accountService.AccountService;

import lombok.AllArgsConstructor;

@RequestMapping("api/accounts")
@AllArgsConstructor
@RestController
public class AccountController {
    private final AccountService accountService;

    private String extractTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        throw new IllegalAccessError("auth header");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequest) {
        LoginResponseDto response = accountService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<AccountDto> addAccount(@RequestBody AccountDto accountDto) {
        AccountDto account = accountService.addAccount(accountDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @GetMapping
    public ResponseEntity<List<AccountDto>> getAllAccounts(
            @RequestHeader("Authorization") String authHeader) {
        String token = extractTokenFromHeader(authHeader);
        List<AccountDto> accounts = accountService.getAllAccounts(token);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccountById(
            @PathVariable String id,
            @RequestHeader("Authorization") String authHeader) {
        String token = extractTokenFromHeader(authHeader);
        AccountDto account = accountService.getAccountById(id, token);
        return ResponseEntity.ok(account);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<AccountDto> updateAccount(
            @PathVariable String id,
            @RequestBody AccountDto accountDto,
            @RequestHeader("Authorization") String authHeader) {
        String token = extractTokenFromHeader(authHeader);
        AccountDto updated = accountService.updateAccount(id, accountDto, token);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") String id){
        accountService.deleteAccount(id);
        return ResponseEntity.ok("Account deleted with id: " + id);
    }
}