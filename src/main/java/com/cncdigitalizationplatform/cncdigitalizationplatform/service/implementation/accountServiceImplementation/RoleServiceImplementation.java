package com.cncdigitalizationplatform.cncdigitalizationplatform.service.implementation.accountServiceImplementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cncdigitalizationplatform.cncdigitalizationplatform.Exceptions.ResourceNotFoundException;
import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.accountDto.RoleDto;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.accountEntities.Role;
import com.cncdigitalizationplatform.cncdigitalizationplatform.mapper.accountMapper.RoleMapper;
import com.cncdigitalizationplatform.cncdigitalizationplatform.repository.accountRepository.RoleRepository;
import com.cncdigitalizationplatform.cncdigitalizationplatform.service.accountService.RoleService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RoleServiceImplementation implements RoleService {
    @Autowired
    RoleRepository roleRepository;
    
    @Autowired
    RoleMapper roleMapper;

    @Override
    public RoleDto addRole(RoleDto roleDto) {
        Role role = roleMapper.toEntity(roleDto);
        Role savedRole = roleRepository.save(role);
        return roleMapper.toDto(savedRole);
    }

    @Override
    public RoleDto updateRole(String id, RoleDto roleDto) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("role"));
        
        roleMapper.updateRoleFromDto(roleDto, role);
        
        role.setUpdatedTime(System.currentTimeMillis());
        Role updated = roleRepository.save(role);
        return roleMapper.toDto(updated);
    }

    @Override
    public void deleteRole(String id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("role"));
        roleRepository.delete(role);
    }

    @Override
    public List<RoleDto> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roleMapper.toDtoList(roles);
    }

    @Override
    public RoleDto getRoleById(String id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("role"));
        return roleMapper.toDto(role);
    }

}
