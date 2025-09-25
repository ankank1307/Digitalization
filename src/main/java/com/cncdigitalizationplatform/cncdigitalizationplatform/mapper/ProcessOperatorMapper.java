package com.cncdigitalizationplatform.cncdigitalizationplatform.mapper;

import com.cncdigitalizationplatform.cncdigitalizationplatform.dto.ProcessOperatorDto;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.ProcessOperator;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.accountEntities.Account;
import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.Process;

public class ProcessOperatorMapper {

    public static ProcessOperator toEntity(ProcessOperatorDto dto, 
                                         Process process, 
                                         Account account) {
        ProcessOperator processOperator = new ProcessOperator();

        processOperator.setProcess(process);
        processOperator.setAccount(account);

        return processOperator;
    }

    public static ProcessOperatorDto toDto(ProcessOperator processOperator) {
        ProcessOperatorDto dto = new ProcessOperatorDto();

        dto.setId(processOperator.getId());
        dto.setProcessId(processOperator.getProcess().getId());
        if (processOperator.getAccount() != null) { 
            dto.setAccountId(processOperator.getAccount().getId());
        }
        dto.setCreatedTime(processOperator.getCreatedTime());

        return dto;
    }
}

