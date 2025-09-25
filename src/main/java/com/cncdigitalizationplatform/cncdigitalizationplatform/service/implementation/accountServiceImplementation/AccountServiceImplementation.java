package com.cncdigitalizationplatform.cncdigitalizationplatform.service.implementation.accountServiceImplementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cncdigitalizationplatform.cncdigitalizationplatform.Exceptions.ResourceNotFoundException;
import com.cncdigitalizationplatform.cncdigitalizationplatform.Exceptions.UnauthorizedException;
import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.accountDto.AccountDto;
import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.accountDto.LoginRequestDto;
import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.accountDto.LoginResponseDto;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.JwtUtil;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.accountEntities.Account;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.accountEntities.AccountSetting;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.accountEntities.Role;
import com.cncdigitalizationplatform.cncdigitalizationplatform.mapper.accountMapper.AccountMapper;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.accountRepository.AccountRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.accountRepository.AccountSettingRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.accountRepository.RoleRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.service.accountService.AccountService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AccountServiceImplementation implements AccountService {
    @Autowired
    AccountMapper accountMapper;
    
    @Autowired
    AccountRepository accountRepository;
    
    @Autowired
    RoleRepository roleRepository;
    
    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    AccountSettingRepository accountSettingRepository;
    
    @Autowired
    PasswordResetServiceImplementation passwordResetServiceImplementation;

    public LoginResponseDto login(LoginRequestDto loginRequest) {
        // Find the account by email
        Account account = accountRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("invalid"));

        // Simple password validation (plain text comparison)
        if (!account.getPassword().equals(loginRequest.getPassword())) {
            throw new IllegalArgumentException("invalid");
        }
        
        if (account.getStatus() != null) {
            if (account.getStatus().equals("inactive")) {
                throw new UnauthorizedException("inactive");
            }
        }
        // Determine role from account's role field or entity type
        String role = account.getRole().getRoleName(); // Use role field first

        // Generate JWT token with accountId, email, and role
        String token = jwtUtil.generateToken(account.getId(), account.getEmail(), role);

        // Create and return response
        LoginResponseDto response = new LoginResponseDto();
        response.setToken(token);
        response.setUserId(account.getId());
        response.setEmail(account.getEmail());
        response.setRole(account.getRole().getRoleShort());
        response.setFullName(account.getFullName());
        response.setPhoneNumber(account.getPhoneNumber());
        response.setAvatarUrl(account.getAvatarUrl());
        
        AccountSetting accountSetting  = accountSettingRepository.findByAccount_Id(account.getId())
            .orElseThrow(() -> new ResourceNotFoundException("setting"));
        response.setLanguage(accountSetting.getLanguage());
        response.setTheme(accountSetting.getTheme());
        
        return response;
    }


    @Override
    public AccountDto addAccount(AccountDto accountRequestDto) {
        if (accountRepository.findByEmail(accountRequestDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("email");
        }
        Role role = roleRepository.findByRoleShort(accountRequestDto.getRole())
            .orElseThrow(() -> new IllegalArgumentException("role"));
        Account account = accountMapper.toEntity(accountRequestDto, role);        
        Account savedAccount = accountRepository.save(account);
        
        AccountSetting accountSetting = new AccountSetting();
        accountSetting.setAccount(savedAccount);
        accountSettingRepository.save(accountSetting);
        
        passwordResetServiceImplementation.initiatePasswordReset(accountRequestDto.getEmail());
        
        return accountMapper.toDto(savedAccount);
    }

    @Override
    public AccountDto updateAccount(String accountId, AccountDto accountRequestDto, String token) {
        // Validate token
        if (!jwtUtil.validateToken(token)) {
            throw new UnauthorizedException("token");
        }
        
        String tokenRole = jwtUtil.getRoleFromToken(token);
        String tokenAccountId = jwtUtil.getAccountIdFromToken(token);
        
        // Admin can update any account, others can only update their own
        if (!"admin".equalsIgnoreCase(tokenRole)) {
            if (!accountId.equals(tokenAccountId)) {
                throw new UnauthorizedException("notAdmin");
            }
            
            // Non-admin users cannot change their own role
            Account existing = accountRepository.findById(accountId)
                    .orElseThrow(() -> new ResourceNotFoundException("account"));
            
            if (accountRequestDto.getRole() != null && !accountRequestDto.getRole().equals(existing.getRole().getRoleName())) {
                throw new UnauthorizedException("role");
            }
        }
        
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("account"));
                
        accountMapper.updateAccountFromDto(accountRequestDto, account);
        if (accountRequestDto.getRole() != null) {
            Role role = roleRepository.findByRoleName(accountRequestDto.getRole()).orElseThrow();
            account.setRole(role);
        } 
        account.setUpdatedTime(System.currentTimeMillis());
        
        Account updated = accountRepository.save(account);
        return accountMapper.toDto(updated);
    }

    @Override
    public void deleteAccount(String id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("account"));
        accountRepository.delete(account);
    }

    @Override
    public List<AccountDto> getAllAccounts(String token) {
         // Validate token
        // if (!jwtUtil.validateToken(token)) {
        //     throw new UnauthorizedException("token");
        // }
        
        // // Check if user has admin role
        // String role = jwtUtil.getRoleFromToken(token);
        // if (!"admin".equalsIgnoreCase(role)) {
        //     throw new UnauthorizedException("admin");
        // }
        
        List<Account> properties = accountRepository.findAll();
        return properties.stream().map(account -> {
            return accountMapper.toDto(account);
        }).toList();
    }

    @Override
    public AccountDto getAccountById(String accountId, String token) {
        // Validate token
        if (!jwtUtil.validateToken(token)) {
            throw new UnauthorizedException("token");
        }
        
        String role = jwtUtil.getRoleFromToken(token);
        String tokenAccountId = jwtUtil.getAccountIdFromToken(token);
        
        // Admin can access any account, others can only access their own
        if (!"admin".equalsIgnoreCase(role)) {
            if (!accountId.equals(tokenAccountId)) {
                throw new UnauthorizedException("notAdmin");
            }
        }
        
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("account"));
        return accountMapper.toDto(account);
    }

}
