package com.cncdigitalizationplatform.cncdigitalizationplatform.mapper.accountMapper;

import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.accountDto.AccountDto;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.accountEntities.Account;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.accountEntities.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface AccountMapper {
    
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);
    
    /**
     * Maps Account entity to AccountDto
     */
    @Mapping(source = "role.roleShort", target = "role")
    AccountDto toDto(Account account);
    
    /**
     * Maps AccountDto to Account entity with Role parameter
     */
    @Mapping(source = "accountDto.id", target = "id")
    @Mapping(source = "accountDto.status", target = "status")
    @Mapping(source = "role", target = "role")
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    @Mapping(target = "accountSetting", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "passwordResetTokens", ignore = true)
    Account toEntity(AccountDto accountDto, Role role);
    
    /**
     * Maps list of Account entities to list of AccountDtos
     */
    List<AccountDto> toDtoList(List<Account> accounts);
    
    /**
     * Updates existing Account entity with data from AccountDto
     * Ignores ID, role, timestamp, and relationship fields
     * Role is handled separately in service layer
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    @Mapping(target = "accountSetting", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "passwordResetTokens", ignore = true)
    void updateAccountFromDto(AccountDto accountDto, @MappingTarget Account account);
}