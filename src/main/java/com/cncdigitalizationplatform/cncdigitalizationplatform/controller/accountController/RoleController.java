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

import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.accountDto.RoleDto;
import com.cncdigitalizationplatform.cncdigitalizationplatform.service.accountService.RoleService;

import lombok.AllArgsConstructor;

@RequestMapping("api/roles")
@AllArgsConstructor
@RestController
public class RoleController {
    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        List<RoleDto> roles = roleService.getAllRoles();
        return ResponseEntity.status(HttpStatus.OK).body(roles);
    }

    @PostMapping
    public ResponseEntity<RoleDto> addRole(@RequestBody RoleDto roleDto) {
        RoleDto role = roleService.addRole(roleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(role);
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<RoleDto> updateRole(@PathVariable("roleId") String roleId, @RequestBody RoleDto roleDto) {
        RoleDto updatedRole = roleService.updateRole(roleId, roleDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedRole);
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<Void> deleteRole(@PathVariable("roleId") String roleId) {
        roleService.deleteRole(roleId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<RoleDto> getRole(@PathVariable("roleId") String roleId) {
        RoleDto role = roleService.getRoleById(roleId);
        return ResponseEntity.status(HttpStatus.OK).body(role);
    }
}