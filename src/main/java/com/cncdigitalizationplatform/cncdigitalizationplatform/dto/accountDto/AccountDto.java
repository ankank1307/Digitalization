package com.cncdigitalizationplatform.cncdigitalizationplatform.dto.accountDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private String id;
    private String fullName;
    private String role;
    private String phoneNumber;
    private String email;
    private Long kpi;
    private String avatarUrl;
    private String status = "active";
    private Long createdTime;
    private Long updatedTime;
}
