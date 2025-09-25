package com.cncdigitalizationplatform.cncdigitalizationplatform.mapper;

import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.ProcessDto;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.DrawingCode;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.Machine;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.Process;
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
public interface ProcessMapper {
    
    ProcessMapper INSTANCE = Mappers.getMapper(ProcessMapper.class);
    
    /**
     * Maps Process entity to ProcessDto
     */
    @Mapping(source = "drawingCode.id", target = "drawingCodeId")
    @Mapping(source = "machine.id", target = "machineId")
    ProcessDto toDto(Process process);
    
    /**
     * Maps ProcessDto to Process entity with relationship parameters
     */
    @Mapping(source = "dto.id", target = "id")
    @Mapping(source = "dto.name", target = "name")
    @Mapping(source = "dto.status", target = "status")
    @Mapping(source = "drawingCode", target = "drawingCode")
    @Mapping(source = "machine", target = "machine")
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    Process toEntity(ProcessDto dto, DrawingCode drawingCode, Machine machine);
    
    /**
     * Maps list of Process entities to list of ProcessDtos
     */
    List<ProcessDto> toDtoList(List<Process> processes);
    
    /**
     * Updates existing Process entity with data from ProcessDto
     * Ignores ID, relationships, and timestamp fields
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "drawingCode", ignore = true)
    @Mapping(target = "machine", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    void updateProcessFromDto(ProcessDto dto, @MappingTarget Process process);
}