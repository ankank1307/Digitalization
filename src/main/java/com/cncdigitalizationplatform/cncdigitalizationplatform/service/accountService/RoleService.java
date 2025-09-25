package com.cncdigitalizationplatform.cncdigitalizationplatform.service.accountService;

import java.util.List;

import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.accountDto.RoleDto;

public interface RoleService {
    RoleDto addRole(RoleDto roleDto);

    RoleDto updateRole(String id, RoleDto roleDto);

    void deleteRole(String id);

    List<RoleDto> getAllRoles();

    RoleDto getRoleById(String id);
}
