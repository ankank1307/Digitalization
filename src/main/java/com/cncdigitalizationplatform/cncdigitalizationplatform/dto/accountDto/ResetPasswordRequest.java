package com.cncdigitalizationplatform.cncdigitalizationplatform.dto.accountDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequest {
    private String token;
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
    private String method;
}
