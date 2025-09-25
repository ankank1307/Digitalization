package com.cncdigitalizationplatform.cncdigitalizationplatform.mapper;

import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.LogDto;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.Log;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.Machine;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.Process;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.accountEntities.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LogMapper {

    LogMapper INSTANCE = Mappers.getMapper(LogMapper.class);

    /**
     * Maps Log entity to LogDto
     */
    @Mapping(source = "machine.id", target = "machineId")
    @Mapping(source = "account.id", target = "accountId")
    @Mapping(source = "process.id", target = "processId")
    LogDto toDto(Log log);

    /**
     * Maps LogDto to Log entity with relationship parameters
     */
    @Mapping(source = "logDto.id", target = "id")
    @Mapping(source = "machine", target = "machine")
    @Mapping(source = "account", target = "account")
    @Mapping(source = "process", target = "process")
    Log toEntity(LogDto logDto, Machine machine, Account account, Process process);

    /**
     * Maps list of Log entities to list of LogDtos
     */
    List<LogDto> toDtoList(List<Log> logs);
}