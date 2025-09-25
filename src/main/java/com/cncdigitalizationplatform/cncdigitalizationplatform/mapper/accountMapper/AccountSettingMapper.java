package com.cncdigitalizationplatform.cncdigitalizationplatform.mapper.accountMapper;

import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.accountDto.AccountSettingDto;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.accountEntities.Account;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.accountEntities.AccountSetting;
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
public interface AccountSettingMapper {
    
    AccountSettingMapper INSTANCE = Mappers.getMapper(AccountSettingMapper.class);
    
    /**
     * Maps AccountSetting entity to AccountSettingDto
     * Maps nested account ID from the Account entity
     */
    @Mapping(source = "account.id", target = "accountId")
    AccountSettingDto toDto(AccountSetting accountSetting);
    
    /**
     * Maps AccountSettingDto to AccountSetting entity with Account parameter
     * Automatic field mapping for matching names
     */
    @Mapping(source = "accountSettingDto.id", target = "id")
    @Mapping(source = "accountSettingDto.status", target = "status")
    @Mapping(source = "account", target = "account")
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    AccountSetting toEntity(AccountSettingDto accountSettingDto, Account account);
    
    /**
     * Maps list of AccountSetting entities to list of AccountSettingDtos
     */
    List<AccountSettingDto> toDtoList(List<AccountSetting> accountSettings);

    /**
     * Updates existing AccountSetting entity with data from AccountSettingDto
     * Only updates non-null fields from the DTO (partial update)
     * Ignores ID, account, and timestamp fields
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    void updateAccountSettingFromDto(AccountSettingDto accountSettingDto, @MappingTarget AccountSetting accountSetting);
}