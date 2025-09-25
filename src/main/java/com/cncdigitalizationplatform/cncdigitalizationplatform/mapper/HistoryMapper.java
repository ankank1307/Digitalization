package com.cncdigitalizationplatform.cncdigitalizationplatform.mapper;

import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.HistoryDto;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.History;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.accountEntities.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface HistoryMapper {
    
    HistoryMapper INSTANCE = Mappers.getMapper(HistoryMapper.class);
    
    /**
     * Maps History entity to HistoryDto
     */
    @Mapping(source = "account.id", target = "accountId")
    HistoryDto toDto(History history);
    
    /**
     * Maps HistoryDto to History entity with Account parameter
     */
    @Mapping(source = "historyDto.id", target = "id")
    @Mapping(source = "account", target = "account")
    @Mapping(target = "time", ignore = true)
    History toEntity(HistoryDto historyDto, Account account);
    
    /**
     * Maps list of History entities to list of HistoryDtos
     */
    List<HistoryDto> toDtoList(List<History> histories);
}