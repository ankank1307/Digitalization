package com.cncdigitalizationplatform.cncdigitalizationplatform.service.accountService;

import java.util.List;

import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.accountDto.AccountDto;
import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.accountDto.LoginRequestDto;
import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.accountDto.LoginResponseDto;

public interface AccountService {
    AccountDto addAccount(AccountDto accountDto);

    AccountDto updateAccount(String id, AccountDto accountDto, String token);

    void deleteAccount(String id);

    List<AccountDto> getAllAccounts(String token);

    AccountDto getAccountById(String id, String token);
    
    LoginResponseDto login(LoginRequestDto loginRequest);
}
