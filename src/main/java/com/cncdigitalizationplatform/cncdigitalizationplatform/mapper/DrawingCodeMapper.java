package com.cncdigitalizationplatform.cncdigitalizationplatform.mapper;

import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.DrawingCodeDto;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.DrawingCode;
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
public interface DrawingCodeMapper {
    
    DrawingCodeMapper INSTANCE = Mappers.getMapper(DrawingCodeMapper.class);
    
    /**
     * Maps DrawingCode entity to DrawingCodeDto
     */
    DrawingCodeDto toDto(DrawingCode drawingCode);
    
    /**
     * Maps DrawingCodeDto to DrawingCode entity
     * Ignores timestamp fields as they are managed by the database
     */
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    DrawingCode toEntity(DrawingCodeDto drawingCodeDto);
    
    /**
     * Maps list of DrawingCode entities to list of DrawingCodeDtos
     */
    List<DrawingCodeDto> toDtoList(List<DrawingCode> drawingCodes);
    
    /**
     * Updates existing DrawingCode entity with data from DrawingCodeDto
     * Ignores ID and timestamp fields
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    void updateDrawingCodeFromDto(DrawingCodeDto drawingCodeDto, @MappingTarget DrawingCode drawingCode);
}