package com.cncdigitalizationplatform.cncdigitalizationplatform.repository.accountRepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.accountEntities.Role;

public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByRoleName(String roleName);
    Optional<Role> findByRoleShort(String roleShort);
}
