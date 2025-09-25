package com.cncdigitalizationplatform.cncdigitalizationplatform.service.accountService;

import java.util.List;

import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.accountDto.AccountSettingDto;

public interface AccountSettingService {
    AccountSettingDto addAccountSetting(AccountSettingDto roleDto);

    AccountSettingDto updateAccountSetting(String id, AccountSettingDto roleDto);

    void deleteAccountSetting(String id);

    List<AccountSettingDto> getAllAccountSettings();

    AccountSettingDto getAccountSettingById(String id);
    
    AccountSettingDto getByAccountId(String accountId);
}
