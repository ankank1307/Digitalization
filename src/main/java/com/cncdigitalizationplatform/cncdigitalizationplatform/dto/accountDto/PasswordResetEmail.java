package com.cncdigitalizationplatform.cncdigitalizationplatform.dto.accountDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetEmail {
    private String username;
    private String email;
}
