package com.cncdigitalizationplatform.cncdigitalizationplatform.service.implementation.accountServiceImplementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cncdigitalizationplatform.cncdigitalizationplatform.Exceptions.ResourceNotFoundException;
import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.accountDto.AccountSettingDto;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.accountEntities.Account;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.accountEntities.AccountSetting;
import com.cncdigitalizationplatform.cncdigitalizationplatform.mapper.accountMapper.AccountSettingMapper;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.accountRepository.AccountRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.accountRepository.AccountSettingRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.service.accountService.AccountSettingService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AccountSettingServiceImplementation implements AccountSettingService {

    @Autowired
    private AccountSettingRepository accountSettingRepository;

    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private AccountSettingMapper accountSettingMapper;

    @Override
    public AccountSettingDto addAccountSetting(AccountSettingDto accountSettingDto) {
        Account account = accountRepository.findById(accountSettingDto.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("account"));

        AccountSetting accountSetting = accountSettingMapper.toEntity(accountSettingDto, account);
        AccountSetting savedSetting = accountSettingRepository.save(accountSetting);

        return accountSettingMapper.toDto(savedSetting);
    }

    @Override
    public List<AccountSettingDto> getAllAccountSettings() {
        return accountSettingMapper.toDtoList(accountSettingRepository.findAll());
    }

    @Override
    public AccountSettingDto getAccountSettingById(String id) {
        AccountSetting setting = accountSettingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("setting"));

        return accountSettingMapper.toDto(setting);
    }

    @Override
    public AccountSettingDto updateAccountSetting(String id, AccountSettingDto accountSettingDto) {
        AccountSetting existingSetting = accountSettingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("setting"));
        
        accountSettingMapper.updateAccountSettingFromDto(accountSettingDto, existingSetting);
        existingSetting.setUpdatedTime(System.currentTimeMillis());
        AccountSetting updatedSetting = accountSettingRepository.save(existingSetting);
        
        return accountSettingMapper.toDto(updatedSetting);
    }

    @Override
    public void deleteAccountSetting(String id) {
        AccountSetting accountSetting = accountSettingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("setting"));
        accountSettingRepository.delete(accountSetting);
    }

    @Override
    public AccountSettingDto getByAccountId(String accountId) {
        AccountSetting setting = accountSettingRepository.findByAccount_Id(accountId)
                .orElseThrow(() -> new RuntimeException("setting"));

        return accountSettingMapper.toDto(setting);
    }
}

