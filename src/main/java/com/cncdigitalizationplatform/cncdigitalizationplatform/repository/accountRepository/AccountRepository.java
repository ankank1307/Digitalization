package com.cncdigitalizationplatform.cncdigitalizationplatform.repository.accountRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.accountEntities.Account;

public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findByEmail(String username);

    List<Account> findByRole_RoleName(String roleName);

    boolean existsByEmail(String email);
}
