package com.cncdigitalizationplatform.cncdigitalizationplatform.dto.accountDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountSettingDto {
    private String id;
    private String accountId;
    private String theme;
    private String language;
    private String status;
    private Long createdTime;
    private Long updatedTime;
}
