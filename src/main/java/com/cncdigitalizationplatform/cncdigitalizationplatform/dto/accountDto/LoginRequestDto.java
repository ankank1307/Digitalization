package com.cncdigitalizationplatform.cncdigitalizationplatform.dto.accountDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {
    private String email; // or email, depending on your login method
    private String password;
}