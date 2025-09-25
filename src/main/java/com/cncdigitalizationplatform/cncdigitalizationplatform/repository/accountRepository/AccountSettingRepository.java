package com.cncdigitalizationplatform.cncdigitalizationplatform.repository.accountRepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.accountEntities.AccountSetting;

public interface AccountSettingRepository extends JpaRepository<AccountSetting, String>{
    Optional<AccountSetting> findByAccount_Id(String accountId);
}
