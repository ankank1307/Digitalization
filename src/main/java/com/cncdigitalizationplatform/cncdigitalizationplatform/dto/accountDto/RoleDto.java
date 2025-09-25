package com.cncdigitalizationplatform.cncdigitalizationplatform.dto.accountDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {
    private String roleId;
    private String roleName;
    private String roleShort;
    private String status;
    private Long createdTime;
    private Long updatedTime;
}
