package com.cncdigitalizationplatform.cncdigitalizationplatform.mapper.accountMapper;

import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.accountDto.RoleDto;
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
public interface RoleMapper {
    
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);
    
    /**
     * Maps Role entity to RoleDto
     */
    RoleDto toDto(Role role);
    
    /**
     * Maps RoleDto to Role entity
     * Ignores timestamp fields as they are managed by the database
     */
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    @Mapping(target = "accounts", ignore = true)
    Role toEntity(RoleDto roleDto);
    
    /**
     * Maps list of Role entities to list of RoleDtos
     */
    List<RoleDto> toDtoList(List<Role> roles);
    
    /**
     * Maps list of RoleDtos to list of Role entities
     */
    List<Role> toEntityList(List<RoleDto> roleDtos);
    
    /**
     * Updates existing Role entity with data from RoleDto
     * Ignores ID and timestamp fields
     */
    @Mapping(target = "roleId", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    @Mapping(target = "accounts", ignore = true)
    void updateRoleFromDto(RoleDto roleDto, @MappingTarget Role role);
}