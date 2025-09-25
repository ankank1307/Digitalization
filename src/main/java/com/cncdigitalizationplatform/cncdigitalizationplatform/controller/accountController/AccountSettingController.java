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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.accountDto.AccountSettingDto;
import com.cncdigitalizationplatform.cncdigitalizationplatform.service.accountService.AccountSettingService;

import lombok.AllArgsConstructor;

@RequestMapping("api/account-settings")
@AllArgsConstructor
@RestController
public class AccountSettingController {
    private final AccountSettingService accountSettingService;

    @GetMapping
    public ResponseEntity<List<AccountSettingDto>> getAllAccountSettings() {
        List<AccountSettingDto> accountSettings = accountSettingService.getAllAccountSettings();
        return ResponseEntity.status(HttpStatus.OK).body(accountSettings);
    }

    @PostMapping
    public ResponseEntity<AccountSettingDto> addAccountSetting(@RequestBody AccountSettingDto accountSettingDto) {
        AccountSettingDto accountSetting = accountSettingService.addAccountSetting(accountSettingDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(accountSetting);
    }

    @PutMapping("/{accountSettingId}")
    public ResponseEntity<AccountSettingDto> updateAccountSetting(@PathVariable("accountSettingId") String accountSettingId, @RequestBody AccountSettingDto accountSettingDto) {
        AccountSettingDto updatedAccountSetting = accountSettingService.updateAccountSetting(accountSettingId, accountSettingDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedAccountSetting);
    }

    @DeleteMapping("/{accountSettingId}")
    public ResponseEntity<Void> deleteAccountSetting(@PathVariable("accountSettingId") String accountSettingId) {
        accountSettingService.deleteAccountSetting(accountSettingId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{accountSettingId}")
    public ResponseEntity<AccountSettingDto> getAccountSetting(@PathVariable("accountSettingId") String accountSettingId) {
        AccountSettingDto accountSetting = accountSettingService.getAccountSettingById(accountSettingId);
        return ResponseEntity.status(HttpStatus.OK).body(accountSetting);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<AccountSettingDto> getAccountSettingByAccount(@PathVariable("accountId") String accountId) {
        AccountSettingDto accountSetting = accountSettingService.getByAccountId(accountId);
        return ResponseEntity.status(HttpStatus.OK).body(accountSetting);
    }
}
