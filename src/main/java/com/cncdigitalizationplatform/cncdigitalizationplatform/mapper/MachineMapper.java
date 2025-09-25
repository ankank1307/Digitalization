package com.cncdigitalizationplatform.cncdigitalizationplatform.mapper;

import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.MachineDto;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.Machine;
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
public interface MachineMapper {
    
    MachineMapper INSTANCE = Mappers.getMapper(MachineMapper.class);
    
    /**
     * Maps Machine entity to MachineDto
     */
    MachineDto toDto(Machine machine);
    
    /**
     * Maps MachineDto to Machine entity
     * Ignores timestamp fields as they are managed by the database
     */
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    Machine toEntity(MachineDto machineDto);
    
    /**
     * Maps list of Machine entities to list of MachineDtos
     */
    List<MachineDto> toDtoList(List<Machine> machines);
    
    /**
     * Updates existing Machine entity with data from MachineDto
     * Ignores ID and timestamp fields
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    void updateMachineFromDto(MachineDto machineDto, @MappingTarget Machine machine);
}