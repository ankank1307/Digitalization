package com.cncdigitalizationplatform.cncdigitalizationplatform.dto.accountDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {
    private String token;
    private String userId;
    private String email;
    private String role;
    private String theme;
    private String language;
    private String avatarUrl;
    private String phoneNumber;
    private String fullName;
}